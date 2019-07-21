package input;

import java.io.*;

/**
 * DiskFileHandler
 *
 * @author isHudw dejavdwuh
 */

public class DiskFileHandler implements FileHandler {
    private StringBuffer sourceCode = new StringBuffer();
    private int curPos = 0;

    @Override
    public void open() {
        //TODO File path selection and Actually read directly into the file
        File file = new File("src/testInput.c");
        try {
            String encoding = "UTF-8";
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;

            while ((lineTxt = bufferedReader.readLine()) != null) {
                sourceCode.append(lineTxt);
            }
            bufferedReader.close();
            read.close();
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }

    @Override
    public int read(byte[] buf, int begin, int len) {
        if (curPos >= sourceCode.length()) {
            return 0;
        }

        int readCount = 0;
        byte[] sourceBuf = sourceCode.toString().getBytes();
        while ((curPos + readCount < sourceCode.length()) && readCount < len) {
            buf[begin + readCount] = sourceBuf[curPos + readCount];
            readCount++;
        }
        curPos += readCount;
        return readCount;
    }

    @Override
    public int close() {
        return 0;
    }

    public static void main(String[] args) {
        DiskFileHandler d = new DiskFileHandler();
        d.open();
        System.out.println(d.sourceCode);
        byte bs[] = new byte[20];
        d.read(bs, 2, 13);
        System.out.println(new String(bs));
    }

}
