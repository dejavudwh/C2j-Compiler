package codegen.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;

public class CodeGenerator {
    private PrintWriter bytecodeFile;
    private int instructionCount = 0;
    private boolean buffered = false;
    private boolean classDefine = false;
    private String bufferedContent = "";
    private String classDefinition = "";
    protected static String programName = "C2Bytecode";
    private HashMap<String, String> nameToDeclaration = new HashMap<>();

    public void setNameAndDeclaration(String name, String declaration) {
        nameToDeclaration.put(name, declaration);
    }

    public String getDeclarationByName(String name) {
        return nameToDeclaration.get(name);
    }

    public void setInstructionBuffered(boolean isBuffer) {
        this.buffered = isBuffer;
    }

    public void setClassDefinition(boolean isClass) {
        this.classDefine = isClass;
    }

    public CodeGenerator() {

        String assemblyFileName = programName + ".j";

        try {
            bytecodeFile = new PrintWriter(new PrintStream(new
                    File(assemblyFileName)));
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

    protected void emitBufferedContent() {
        if (!bufferedContent.isEmpty()) {
            bytecodeFile.println(bufferedContent);
            bytecodeFile.flush();
        }
    }

    protected void emitClassDefinition() {
        if (!classDefinition.isEmpty()) {
            bytecodeFile.println(classDefinition);
            bytecodeFile.flush();
        }
    }

    public void emitDirective(Directive directive) {
        if (buffered) {
            bufferedContent += directive.toString() + "\n";
            return;
        }

        if (classDefine) {
            classDefinition += directive.toString() + "\n";
            return;
        }

        bytecodeFile.println(directive.toString());
        bytecodeFile.flush();
        ++instructionCount;
    }

    public void emitDirective(Directive directive, String operand) {
        if (buffered) {
            bufferedContent += directive.toString() + " " + operand + "\n";
            return;
        }

        if (classDefine) {
            classDefinition += directive.toString() + " " + operand + "\n";
            return;
        }

        bytecodeFile.println(directive.toString() + " " + operand);
        bytecodeFile.flush();
        ++instructionCount;
    }

    public void emitDirective(Directive directive, int operand) {
        if (buffered) {
            bufferedContent += directive.toString() + " " + operand + "\n";
            return;
        }

        if (classDefine) {
            classDefinition += directive.toString() + " " + operand + "\n";
            return;
        }

        bytecodeFile.println(directive.toString() + " " + operand);
        ++instructionCount;
    }

    public void emitDirective(Directive directive, String operand1, String operand2) {
        if (buffered) {
            bufferedContent += directive.toString() + " " + operand1 + " " + operand2 + "\n";
            return;
        }

        if (classDefine) {
            classDefinition += directive.toString() + " " + operand1 + " " + operand2 + "\n";
            return;
        }

        bytecodeFile.println(directive.toString() + " " + operand1 + " " + operand2);
        ++instructionCount;
    }

    public void emitDirective(Directive directive, String operand1, String operand2, String operand3) {
        if (buffered) {
            bufferedContent += directive.toString() + " " + operand1 + " " + operand2 + " " + operand3 + "\n";
            return;
        }

        if (classDefine) {
            classDefinition += directive.toString() + " " + operand1 + " " + operand2 + " " + operand3 + "\n";
            return;
        }

        bytecodeFile.println(directive.toString() + " " + operand1 + " " + operand2 + " " + operand3);
        ++instructionCount;
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
