package input;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Input class
 *
 * @author isHudw dejavudwh
 */

public class Input {
    private final int MAXLOOK = 16;
    private final int MAXLEX = 1024;
    private final int BUFSIZE =  (MAXLEX * 3 ) + (2 * MAXLOOK);
    private int bufferEndFlag = BUFSIZE;
    private final int DANGER = bufferEndFlag - MAXLOOK;
    private final int END = BUFSIZE;
    private boolean readEof = false;
    private final byte[] inputBuf = new byte[BUFSIZE];

    private int next = END;
    private int startCurCharPos = END;
    private int endCurCharPos = END;
    private int curCharLineno = 1;

    private FileHandler fileHandler = null;

    private boolean isReadEnd() {
        return (readEof && next >= bufferEndFlag);
    }

    private FileHandler getFileHandler() {
        //TODO Select a different file handle
        return new DiskFileHandler();
    }

    public void newFile() {
        this.fileHandler = getFileHandler();
        fileHandler.open();

        readEof = false;
        next = END;
        startCurCharPos = END;
        endCurCharPos = END;
        curCharLineno = 1;
    }

    public String getCurText() {
        int len = endCurCharPos - startCurCharPos;
        byte[] str = Arrays.copyOfRange(inputBuf, startCurCharPos, endCurCharPos + len);
        return new String(str, StandardCharsets.UTF_8);
    }

    public byte inputAdvance() {
        char enter = '\n';

        if (isReadEnd()) {
            return 0;
        }

        if (!readEof && flush(false) < 0) {
            //缓冲区出错
            return -1;
        }

        if (inputBuf[next] == enter) {
            curCharLineno++;
        }
        endCurCharPos++;

        return inputBuf[next++];
    }

    private int flush(boolean force) {
        int noMoreCharToRead = 0;
        int flushOk = 1;

        int shiftPart, copyPart, leftEdge;
        if (isReadEnd()) {
            return noMoreCharToRead;
        }

        if (readEof) {
            return flushOk;
        }

        if (next > DANGER || force) {
            leftEdge = next;
            copyPart = bufferEndFlag - leftEdge;
            System.arraycopy(inputBuf, leftEdge, inputBuf, 0, copyPart);
            if (fillBuf(copyPart) == 0) {
                System.err.println("Internal Error, flush: Buffer full, can't read");
            }

            startCurCharPos -= leftEdge;
            endCurCharPos -= leftEdge;
            next  -= leftEdge;
        }
        return flushOk;
    }

    private int fillBuf(int startPos) {
        int need;
        int got;
        need = END - startPos;
        if (need < 0) {
            System.err.println("Internal Error (fill buf): Bad read-request starting addr.");
        }

        if (need == 0) {
            return 0;
        }

        if ((got = fileHandler.read(inputBuf, startPos, need)) == -1) {
            System.err.println("Can't read input file");
        }

        bufferEndFlag = startPos + got;
        if (got < need) {
            //输入流已经到末尾
            readEof = true;
        }

        return got;
    }

    public String getWholeInput() {
        //TODO The equivalent of input system doesn't work
        return fileHandler.getSourceCode().toString();
    }

    public static void main(String[] args) {
        Input input = new Input();
        input.newFile();
        for (int i = 0; i < 30; i++) {
            byte b = input.inputAdvance();
            System.out.println(b + "  " + (char) b);
        }
    }

}
