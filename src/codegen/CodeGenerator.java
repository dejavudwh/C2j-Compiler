package codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @author dejavudwh isHudw
 */

public class CodeGenerator {
    private PrintWriter bytecodeFile;
    private int instructionCount = 0;
    private boolean buffered = false;
    private boolean classDefine = false;
    private String bufferedContent = "";
    private String classDefinition = "";
    protected static String programName = "C2Bytecode";
    private HashMap<String, String> nameToDeclaration = new HashMap<String, String>();

    public CodeGenerator() {
        String fileName = programName + ".j";
        try {
            bytecodeFile = new PrintWriter(new PrintStream(new
                    File(fileName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void emitString(String s) {
        if (buffered) {
            bufferedContent += s + "\n";
            return;
        }

        if (classDefine) {
            classDefinition += s + "\n";
            return;
        }

        bytecodeFile.print(s);
        bytecodeFile.flush();
    }

    public void emit(Instruction opcode) {
        if (buffered) {
            bufferedContent += "\t" + opcode.toString() + "\n";
            return;
        }

        if (classDefine) {
            classDefinition += "\t" + opcode.toString() + "\n";
            return;
        }

        bytecodeFile.println("\t" + opcode.toString());
        bytecodeFile.flush();
        ++instructionCount;
    }

    public void emit(Instruction opcode, String operand) {
        if (buffered) {
            bufferedContent += "\t" + opcode.toString() + "\t" + operand + "\n";
            return;
        }

        if (classDefine) {
            classDefinition += "\t" + opcode.toString() + "\t" + operand + "\n";
            return;
        }

        bytecodeFile.println("\t" + opcode.toString() + "\t" + operand);
        bytecodeFile.flush();
        ++instructionCount;
    }

    public void emitBlankLine() {
        if (buffered) {
            bufferedContent += "\n";
            return;
        }

        if (classDefine) {
            classDefinition += "\n";
            return;
        }

        bytecodeFile.println();
        bytecodeFile.flush();
    }

    public void finish() {
        bytecodeFile.close();
    }


}

