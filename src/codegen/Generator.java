package codegen;

import symboltable.*;

import java.util.*;

/**
 * @author dejavudwh isHudw
 */

public class Generator extends CodeGenerator {
    private static Generator instance = null;

    private Stack<String> nameStack = new Stack<>();
    private Map<String, String> arrayNameMap = new HashMap<>();
    private boolean isInitArguments = false;
    private ArrayList<String> structNameList = new ArrayList<>();
    private int branch_count = 0;
    private int branch_out = 0;
    /**
     * If nesting level, an if plus an I
     */
    private String embedded = "";
    private String comparingCmd = "";

    private int loopCount = 0;

    private Generator() {

    }

    public static Generator getInstance() {
        if (instance == null) {
            instance = new Generator();
        }

        return instance;
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

        /* If it's an assignment between arrays */
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
            Generator generator = Generator.getInstance();
            int i = generator.getLocalVariableIndex((Symbol) value);
            generator.emit(Instruction.ILOAD, "" + i);
        }

        this.emit(Instruction.IASTORE);
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

    public int getIfElseEmbedLevel() {
        return embedded.length();
    }

    public void increaseIfElseEmbed() {
        embedded += "i";
    }

    public void decreaseIfElseEmbed() {
        embedded = embedded.substring(1);
    }

    public void setComparingCommand(String cmd) {
        comparingCmd = cmd;
    }

    public void emitComparingCommand() {
        emitString(comparingCmd);
    }

    public String getBranchOut() {
        return embedded + "branch_out" + branch_out;
    }

    public String getCurrentBranch() {
        return embedded + "branch" + branch_count;
    }

    public void setCurrentFuncName(String name) {
        nameStack.push(name);
    }

    public String getCurrentFuncName() {
        return nameStack.peek();
    }

    public void increaseBranch() {
        branch_count++;
    }

    public void emitBranchOut() {
        String s = "\n" + embedded + "branch_out" + branch_out + ":\n";
        this.emitString(s);
        branch_out++;
    }

    public void initFuncArguments(boolean b) {
        isInitArguments = b;
    }

    public boolean isPassingArguments() {
        return isInitArguments;
    }

    public String getProgramName() {
        return programName;
    }

    public void createStructArray(Symbol structSymArray) {
        //先判断数组是否已经创建过，如果创建过，那么直接返回
        Specifier sp = structSymArray.getSpecifierByType(Specifier.STRUCTURE);
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

        Declarator declarator = structSymArray.getDeclarator(Declarator.ARRAY);
        int eleCount = declarator.getElementNum();
        this.emit(Instruction.SIPUSH, "" + eleCount);
        this.emit(Instruction.ANEWARRAY, struct.getTag());

        int idx = getLocalVariableIndex(structSymArray);
        this.emit(Instruction.ASTORE, "" + idx);

        declareStructAsClass(struct);
    }

    private void declareStructAsClass(StructDefine struct) {
        //这条语句的作用是，把接下来生成的指令先缓存起来，而不是直接写入到文件里
        this.setClassDefinition(true);

        this.emitDirective(Directive.CLASS_PUBLIC, struct.getTag());
        this.emitDirective(Directive.SUPER, "java/lang/Object");

        /*
         * 把结构体中的每个成员转换成相应的具有public性质的java类成员
         */
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

        /*
         * 实现类的初始构造函数，它调用父类的构造函数后，接下来通过putfield指令，把类的每个成员都初始化为0
         */
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

    public void createInstanceForStructArray(Symbol structSymArray, int idx) {
        //先把结构体数组对象加载到堆栈上
        int i = getLocalVariableIndex(structSymArray);
        this.emit(Instruction.ALOAD, "" + i);

        //把构造的实例对象放置到数组对应下标
        this.emit(Instruction.SIPUSH, "" + idx);

        //先构造一个结构体实例对象
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
            //结构体对象来自于结构体数组，此时需要把结构体数组对象加载到堆栈
            structSym = vs.getSymbol();
        }
        /*
         * 先把类的实例加载到堆栈顶部, 如果结构体来自于数组，那么这里加载到堆栈的就是结构体数组对象
         */
        int idx = getLocalVariableIndex(structSym);
        this.emit(Instruction.ALOAD, "" + idx);

        if (vs != null) {
            //把要修改的结构体对象从结构体数组里加载到堆栈上
            int i = (int) vs.getIndex();
            this.emit(Instruction.SIPUSH, "" + i);
            this.emit(Instruction.AALOAD);
        }

        /*
         * 如果我们要读取myTag.x 下面的语句会构造出
         * CTag/x  I
         */
        String fieldType = "";
        if (field.hasType(Specifier.INT)) {
            fieldType = "I";
        } else if (field.hasType(Specifier.CHAR)) {
            fieldType = "C";
        } else if (field.hasType(Specifier.CHAR) && field.getDeclarator(Declarator.POINTER) != null) {
            fieldType = "Ljava/lang/String;";
        }

        //通过getfield指令把结构体的成员变量读出来后压入堆栈顶部
        Specifier sp = structSym.getSpecifierByType(Specifier.STRUCTURE);
        StructDefine struct = sp.getStruct();
        String fieldContent = struct.getTag() + "/" + field.getName() + " " + fieldType;
        this.emit(Instruction.GETFIELD, fieldContent);
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
        StructDefine struct = sp.getStructObj();
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
            this.emit(Instruction.INVOKESPECIAL, struct.getTag()+"/"+"<init>()V");
            int idx = this.getLocalVariableIndex(symbol);
            this.emit(Instruction.ASTORE, ""+idx);
        }

        declareStructAsClass(struct);
    }

    public void popFuncName() {
        nameStack.pop();
    }

}
