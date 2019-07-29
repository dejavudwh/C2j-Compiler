package codegen.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import codegen.ArrayValueSetter;
import symboltable.*;

public class ProgramGenerator extends CodeGenerator {
    private static ProgramGenerator instance = null;
    private Stack<String> nameStack = new Stack<String>();
    private Map<String, String> arrayNameMap = new HashMap<String, String>();
    private boolean isInitArguments = false;
    private ArrayList<String> structNameList = new ArrayList<String>();
    private int branch_count = 0;
    private int branch_out = 0;
    private String embedded = "";
    private String comparingCmd = "";

    private int loopCount = 0;

    public static ProgramGenerator getInstance() {
        if (instance == null) {
            instance = new ProgramGenerator();
        }

        return instance;
    }

    public int getIfElseEmbedCount() {
        return embedded.length();
    }

    public void incraseIfElseEmbed() {
        embedded += "i";
    }

    public void decraseIfElseEmbed() {
        embedded = embedded.substring(1);
    }

    public void setComparingCommand(String cmd) {
        comparingCmd = cmd;
    }

    public void emitComparingCommand() {
        emitString(comparingCmd);
    }

    public void emitBranchOut() {
        String s = "\n" + embedded + "branch_out" + branch_out + ":\n";
        this.emitString(s);
        branch_out++;
    }

    public String getBranchOut() {
        String s = embedded + "branch_out" + branch_out;
        return s;
    }

    public String getCurrentBranch() {
        String str = embedded + "branch" + branch_count;
        return str;
    }

    public void emitLoopBranch() {
        String s = "\n" + "loop" + loopCount + ":" + "\n";
        emitString(s);
    }

    public String getLoopBranch() {
        return "loop" + loopCount;
    }

    public void increaseLoopCount() {
        loopCount++;
    }

    public void increaseBranch() {
        branch_count++;
    }

    public String getAheadBranch(int ahead) {
        String str = embedded + "branch" + branch_count + ahead + ":";
        this.emitString(str);
        return str;
    }

    public void putStructToClassDeclaration(Symbol symbol) {
        //判断传入的Symbol变量是否是结构体变量，不是的话立刻返回
        Specifier sp = symbol.getSpecifierByType(Specifier.STRUCTURE);
        if (sp == null) {
            return;
        }

        /*
         * 在队列structNameList中查询Symbol对应的结构体名字是否已经存储在队列中，如果在队列中有了
         * 那表明该结构体已经被转换成java类，并且类的定义已经转换成java汇编语言了
         */
        StructDefine struct = sp.getStruct();
        if (structNameList.contains(struct.getTag())) {
            return;
        } else {
            structNameList.add(struct.getTag());
        }

        /*
         * 输出相应指令，把结构体转换成java类
         */
        //如果当前声明的是结构体数组，那么就不用在堆栈上构建一个实例,而是直接从数组中把实例加载到堆栈上
        if (symbol.getValueSetter() == null) {
            this.emit(Instruction.NEW, struct.getTag());
            this.emit(Instruction.DUP);
            this.emit(Instruction.INVOKESPECIAL, struct.getTag() + "/" + "<init>()V");
            int idx = this.getLocalVariableIndex(symbol);
            this.emit(Instruction.ASTORE, "" + idx);
        }

        declareStructAsClass(struct);
    }

    private void declareStructAsClass(StructDefine struct) {
        this.setClassDefinition(true);

        this.emitDirective(Directive.CLASS_PUBLIC, struct.getTag());
        this.emitDirective(Directive.SUPER, "java/lang/Object");

        Symbol fields = struct.getFields();
        do {
            String fieldName = fields.getName() + " ";
            if (fields.getDeclarator(Declarator.ARRAY) != null) {
                fieldName += "[";
            }

            if (fields.hasType(Specifier.INT)) {
                fieldName += "I";
            } else if (fields.hasType(Specifier.CHAR)) {
                fieldName += "C";
            } else if (fields.hasType(Specifier.CHAR) && fields.getDeclarator(Declarator.POINTER) != null) {
                fieldName += "Ljava/lang/String;";
            }

            this.emitDirective(Directive.FIELD_PUBLIC, fieldName);
            fields = fields.getNextSymbol();
        } while (fields != null);

        this.emitDirective(Directive.METHOD_PUBLIC, "<init>()V");
        this.emit(Instruction.ALOAD, "0");
        String superInit = "java/lang/Object/<init>()V";
        this.emit(Instruction.INVOKESPECIAL, superInit);

        fields = struct.getFields();
        do {
            this.emit(Instruction.ALOAD, "0");
            String fieldName = struct.getTag() + "/" + fields.getName();
            String fieldType = "";
            if (fields.hasType(Specifier.INT)) {
                fieldType = "I";
                this.emit(Instruction.SIPUSH, "0");
            } else if (fields.hasType(Specifier.CHAR)) {
                fieldType = "C";
                this.emit(Instruction.SIPUSH, "0");
            } else if (fields.hasType(Specifier.CHAR) && fields.getDeclarator(Declarator.POINTER) != null) {
                fieldType = "Ljava/lang/String;";
                this.emit(Instruction.LDC, " ");
            }

            String classField = fieldName + " " + fieldType;
            this.emit(Instruction.PUTFIELD, classField);

            fields = fields.getNextSymbol();
        } while (fields != null);

        this.emit(Instruction.RETURN);
        this.emitDirective(Directive.END_METHOD);
        this.emitDirective(Directive.END_CLASS);

        this.setClassDefinition(false);
    }

    public void assignValueToStructMember(Symbol structSym, Symbol field, Object val) {
        int idx = getLocalVariableIndex(structSym);
        this.emit(Instruction.ALOAD, "" + idx);

        String value = "";
        String fieldType = "";
        if (field.hasType(Specifier.INT)) {
            fieldType = "I";
            value += (Integer) val;
            this.emit(Instruction.SIPUSH, value);
        } else if (field.hasType(Specifier.CHAR)) {
            fieldType = "C";
            value += (Integer) val;
            this.emit(Instruction.SIPUSH, value);
        } else if (field.hasType(Specifier.CHAR) && field.getDeclarator(Declarator.POINTER) != null) {
            fieldType = "Ljava/lang/String;";
            value += (String) val;
            this.emit(Instruction.LDC, value);
        }

        Specifier sp = structSym.getSpecifierByType(Specifier.STRUCTURE);
        StructDefine struct = sp.getStruct();
        String fieldContent = struct.getTag() + "/" + field.getName() + " " + fieldType;
        this.emit(Instruction.PUTFIELD, fieldContent);
    }

    public void assignValueToStructMemberFromArray(Object obj, Symbol field, Object val) {
        ArrayValueSetter setter = (ArrayValueSetter) obj;
        int idx = (int) setter.getIndex();
        Symbol symbol = setter.getSymbol();

        int i = getLocalVariableIndex(symbol);
        this.emit(Instruction.ALOAD, "" + i);

        this.emit(Instruction.SIPUSH, "" + idx);
        this.emit(Instruction.AALOAD);

        String value = "";
        String fieldType = "";
        if (field.hasType(Specifier.INT)) {
            fieldType = "I";
            value += (Integer) val;
            this.emit(Instruction.SIPUSH, value);
        } else if (field.hasType(Specifier.CHAR)) {
            fieldType = "C";
            value += (Integer) val;
            this.emit(Instruction.SIPUSH, value);
        } else if (field.hasType(Specifier.CHAR) && field.getDeclarator(Declarator.POINTER) != null) {
            fieldType = "Ljava/lang/String;";
            value += (String) val;
            this.emit(Instruction.LDC, value);
        }

        Specifier sp = symbol.getSpecifierByType(Specifier.STRUCTURE);
        StructDefine struct = sp.getStruct();
        String fieldContent = struct.getTag() + "/" + field.getName() + " " + fieldType;
        this.emit(Instruction.PUTFIELD, fieldContent);
    }

    public void createStructArray(Symbol structSymArray) {
        Specifier sp = structSymArray.getSpecifierByType(Specifier.STRUCTURE);

        StructDefine struct = sp.getStruct();
        if (structNameList.contains(struct.getTag())) {
            return;
        } else {
            structNameList.add(struct.getTag());
        }

        Declarator declarator = structSymArray.getDeclarator(Declarator.ARRAY);
        int eleCount = declarator.getElementNum();
        this.emit(Instruction.SIPUSH, "" + eleCount);
        this.emit(Instruction.ANEWARRAY, struct.getTag());

        int idx = getLocalVariableIndex(structSymArray);
        this.emit(Instruction.ASTORE, "" + idx);

        declareStructAsClass(struct);
    }

    public void createInstanceForStructArray(Symbol structSymArray, int idx) {
        int i = getLocalVariableIndex(structSymArray);
        this.emit(Instruction.ALOAD, "" + i);

        this.emit(Instruction.SIPUSH, "" + idx);

        Specifier sp = structSymArray.getSpecifierByType(Specifier.STRUCTURE);
        StructDefine struct = sp.getStruct();
        this.emit(Instruction.NEW, struct.getTag());
        this.emit(Instruction.DUP);
        this.emit(Instruction.INVOKESPECIAL, struct.getTag() + "/" + "<init>()V");

        this.emit(Instruction.AASTORE);
    }

    public void readValueFromStructMember(Symbol structSym, Symbol field) {

        ArrayValueSetter vs = (ArrayValueSetter) structSym.getValueSetter();
        if (vs != null) {
            structSym = vs.getSymbol();
        }

        int idx = getLocalVariableIndex(structSym);
        this.emit(Instruction.ALOAD, "" + idx);

        if (vs != null) {
            int i = (int) vs.getIndex();
            this.emit(Instruction.SIPUSH, "" + i);
            this.emit(Instruction.AALOAD);
        }

        String fieldType = "";
        if (field.hasType(Specifier.INT)) {
            fieldType = "I";
        } else if (field.hasType(Specifier.CHAR)) {
            fieldType = "C";
        } else if (field.hasType(Specifier.CHAR) && field.getDeclarator(Declarator.POINTER) != null) {
            fieldType = "Ljava/lang/String;";
        }

        Specifier sp = structSym.getSpecifierByType(Specifier.STRUCTURE);
        StructDefine struct = sp.getStruct();
        String fieldContent = struct.getTag() + "/" + field.getName() + " " + fieldType;
        this.emit(Instruction.GETFIELD, fieldContent);
    }

    public void initFuncArguments(boolean bool) {
        isInitArguments = bool;
    }

    public boolean isPassingArguments() {
        return isInitArguments;
    }

    public void setCurrentFuncName(String name) {
        nameStack.push(name);
    }

    public String getCurrentFuncName() {
        return nameStack.peek();
    }

    public void popFuncName() {
        nameStack.pop();
    }

    public int getLocalVariableIndex(Symbol symbol) {
        TypeSystem typeSys = TypeSystem.getInstance();
        String funcName = nameStack.peek();
        Symbol funcSym = typeSys.getSymbolByText(funcName, 0, "main");
        ArrayList<Symbol> localVariables = new ArrayList<>();
        Symbol s = funcSym.getArgList();
        while (s != null) {
            localVariables.add(s);
            s = s.getNextSymbol();
        }
        Collections.reverse(localVariables);

        ArrayList<Symbol> list = typeSys.getSymbolsByScope(symbol.getScope());
        for (int i = 0; i < list.size(); i++) {
            if (!localVariables.contains(list.get(i))) {
                localVariables.add(list.get(i));
            }
        }

        for (int i = 0; i < localVariables.size(); i++) {
            if (localVariables.get(i) == symbol) {
                return i;
            }
        }

        return -1;
    }

    public void createArray(Symbol symbol) {
        if (arrayNameMap.containsKey(symbol.getScope())) {
            if (arrayNameMap.get(symbol.getScope()).equals(symbol.getName())) {
                return;
            }
        }

        arrayNameMap.put(symbol.getScope(), symbol.getName());

        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if (declarator == null) {
            return;
        }

        String type = "";
        if (symbol.hasType(Specifier.INT)) {
            type = "int";
        }

        int num = declarator.getElementNum();
        this.emit(Instruction.SIPUSH, "" + num);
        this.emit(Instruction.NEWARRAY, type);
        int idx = getLocalVariableIndex(symbol);
        this.emit(Instruction.ASTORE, "" + idx);
    }

    public void readArrayElement(Symbol symbol, Object index) {
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if (declarator == null) {
            return;
        }

        int idx = getLocalVariableIndex(symbol);
        this.emit(Instruction.ALOAD, "" + idx);

        if (index instanceof Integer) {
            this.emit(Instruction.SIPUSH, "" + index);
        } else if (index instanceof Symbol) {
            int i = this.getLocalVariableIndex((Symbol) index);
            this.emit(Instruction.ILOAD, "" + i);
        }
        this.emit(Instruction.IALOAD);
    }

    public void writeArrayElement(Symbol symbol, Object index, Object value) {
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if (declarator == null) {
            return;
        }

        int idx = getLocalVariableIndex(symbol);
        if (symbol.hasType(Specifier.INT)) {
            this.emit(Instruction.ALOAD, "" + idx);
            if (index instanceof Integer) {
                this.emit(Instruction.SIPUSH, "" + index);
            } else {
                int i = this.getLocalVariableIndex((Symbol) index);
                this.emit(Instruction.ILOAD, "" + i);
            }
        }

        if (value instanceof ArrayValueSetter) {
            ArrayValueSetter setter = (ArrayValueSetter) value;
            Object idxObj = setter.getIndex();
            Symbol arraySym = setter.getSymbol();
            if (idxObj instanceof Integer) {
                int i = (int) idxObj;
                this.readArrayElement(arraySym, i);
            } else {
                this.readArrayElement(arraySym, idxObj);
            }
        }

        if (value instanceof Integer) {
            int val = (int) value;
            this.emit(Instruction.SIPUSH, "" + val);
        } else if (value instanceof Symbol) {
            ProgramGenerator generator = ProgramGenerator.getInstance();
            int i = generator.getLocalVariableIndex((Symbol) value);
            generator.emit(Instruction.ILOAD, "" + i);
        }

        this.emit(Instruction.IASTORE);
    }

    private ProgramGenerator() {

    }

    public String getProgramName() {
        return programName;
    }

    public void generateHeader() {
        emitDirective(Directive.CLASS_PUBLIC, programName);
        emitDirective(Directive.SUPER, "java/lang/Object");
        emitBlankLine();
        emitDirective(Directive.METHOD_PUBBLIC_STATIC, "main([Ljava/lang/String;)V");
    }


    @Override
    public void finish() {
        emit(Instruction.RETURN);
        emitDirective(Directive.END_METHOD);
        emitBufferedContent();
        emitDirective(Directive.END_CLASS);
        emitClassDefinition();
        super.finish();
    }
}
