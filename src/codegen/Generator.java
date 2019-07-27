package codegen;

import symboltable.Declarator;
import symboltable.Specifier;
import symboltable.Symbol;
import symboltable.TypeSystem;

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

    public void increaseBranch() {
        branch_count++;
    }

    public void emitBranchOut() {
        String s = "\n" + embedded + "branch_out" + branch_out + ":\n";
        this.emitString(s);
        branch_out++;
    }

}
