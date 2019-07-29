package codegen;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import ast.AstBuilder;
import ast.AstNode;
import ast.NodeKey;
import codegen.backend.Directive;
import codegen.backend.Instruction;
import codegen.backend.ProgramGenerator;
import interpreter.ValueSetter;
import parse.SyntaxProductionInit;
import symboltable.*;

/**
 * @author dejavudwh isHudw
 */

public class UnaryNodeGenerate extends BaseGenerate implements GenerateReceiver {
    private Symbol structObjSymbol = null;
    private Symbol monitorSymbol = null;

    @Override
    public Object generate(AstNode root) {
        int production = (Integer) root.getAttribute(NodeKey.PRODUCTION);
        generateChildren(root);

        String text;
        Symbol symbol;
        Object value;
        AstNode child;

        switch (production) {
            case SyntaxProductionInit.Number_TO_Unary:
                text = (String) root.getAttribute(NodeKey.TEXT);
                boolean isFloat = text.indexOf('.') != -1;
                if (isFloat) {
                    value = Float.valueOf(text);
                    root.setAttribute(NodeKey.VALUE, value);
                } else {
                    value = Integer.valueOf(text);
                    root.setAttribute(NodeKey.VALUE, value);
                }
                break;

            case SyntaxProductionInit.Name_TO_Unary:
                symbol = (Symbol) root.getAttribute(NodeKey.SYMBOL);
                if (symbol != null) {
                    root.setAttribute(NodeKey.VALUE, symbol.getValue());
                    root.setAttribute(NodeKey.TEXT, symbol.getName());
                }
                break;

            case SyntaxProductionInit.String_TO_Unary:
                text = (String) root.getAttribute(NodeKey.TEXT);
                root.setAttribute(NodeKey.VALUE, text);
                break;

            case SyntaxProductionInit.Unary_LB_Expr_RB_TO_Unary:
                child = root.getChildren().get(0);
                symbol = (Symbol) child.getAttribute(NodeKey.SYMBOL);

                child = root.getChildren().get(1);
                int index = 0;
                if (child.getAttribute(NodeKey.VALUE) != null) {
                    index = (Integer) child.getAttribute(NodeKey.VALUE);
                }
                Object idxObj = child.getAttribute(NodeKey.SYMBOL);

                try {
                    Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
                    if (declarator != null) {
                        Object val = declarator.getElement((int) index);
                        root.setAttribute(NodeKey.VALUE, val);
                        ArrayValueSetter setter;
                        if (idxObj == null) {
                            setter = new ArrayValueSetter(symbol, index);
                        } else {
                            setter = new ArrayValueSetter(symbol, idxObj);
                        }

                        root.setAttribute(NodeKey.SYMBOL, setter);
                        root.setAttribute(NodeKey.TEXT, symbol.getName());

                    }
                    Declarator pointer = symbol.getDeclarator(Declarator.POINTER);
                    if (pointer != null) {
                        setPointerValue(root, symbol, index);
                        PointerValueSetter pv = new PointerValueSetter(symbol, index);
                        root.setAttribute(NodeKey.SYMBOL, pv);
                        root.setAttribute(NodeKey.TEXT, symbol.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                break;

            case SyntaxProductionInit.Unary_Incop_TO_Unary:
            case SyntaxProductionInit.Unary_DecOp_TO_Unary:
                symbol = (Symbol) root.getChildren().get(0).getAttribute(NodeKey.SYMBOL);
                Integer val = (Integer) symbol.getValue();
                ValueSetter setter;
                setter = (ValueSetter) symbol;
                int i = generator.getLocalVariableIndex(symbol);
                generator.emit(Instruction.ILOAD, "" + i);
                generator.emit(Instruction.SIPUSH, "" + 1);

                try {
                    if (production == SyntaxProductionInit.Unary_Incop_TO_Unary) {
                        generator.emit(Instruction.IADD);
                    } else {
                        generator.emit(Instruction.ISUB);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Runtime Error: Assign Value Error");
                }
                generator.emit(Instruction.ISTORE, "" + i);
                break;

            case SyntaxProductionInit.LP_Expr_RP_TO_Unary:
                child = root.getChildren().get(0);
                copyChild(root, child);
                break;

            case SyntaxProductionInit.Start_Unary_TO_Unary:
                //Compilation to bytecode does not support Pointers
                child = root.getChildren().get(0);
                int addr = (Integer) child.getAttribute(NodeKey.VALUE);
                symbol = (Symbol) child.getAttribute(NodeKey.SYMBOL);

                MemoryHeap memHeap = MemoryHeap.getInstance();
                Map.Entry<Integer, byte[]> entry = memHeap.getMem(addr);
                int offset = addr - entry.getKey();
                if (entry != null) {
                    byte[] memByte = entry.getValue();
                    root.setAttribute(NodeKey.VALUE, memByte[offset]);
                }

                DirectMemValueSetter directMemSetter = new DirectMemValueSetter(addr);
                root.setAttribute(NodeKey.SYMBOL, directMemSetter);

                break;

            case SyntaxProductionInit.Unary_LP_RP_TO_Unary:
            case SyntaxProductionInit.Unary_LP_ARGS_RP_TO_Unary:
                boolean reEntry = false;
                String funcName = (String) root.getChildren().get(0).getAttribute(NodeKey.TEXT);

                if (funcName != "" && funcName.equals(BaseGenerate.funcName)) {
                    reEntry = true;
                }

                ArrayList<Object> argList = null;
                ArrayList<Object> symList = null;

                if (production == SyntaxProductionInit.Unary_LP_ARGS_RP_TO_Unary) {
                    AstNode argsNode = root.getChildren().get(1);
                    argList = (ArrayList<Object>) argsNode.getAttribute(NodeKey.VALUE);
                    symList = (ArrayList<Object>) argsNode.getAttribute(NodeKey.SYMBOL);
                    FunctionArgumentList.getFunctionArgumentList().setFuncArgList(argList);
                    FunctionArgumentList.getFunctionArgumentList().setFuncArgSymbolList(symList);
                }

                AstNode func = AstBuilder.getInstance().getFunctionNodeByName(funcName);
                if (func != null) {
                    BaseGenerate.funcName = funcName;
                    int count = 0;
                    while (count < argList.size()) {
                        Object objVal = argList.get(count);
                        Object objSym = symList.get(count);
                        if (objSym != null) {
                            Symbol param = (Symbol) objSym;
                            int idx = generator.getLocalVariableIndex(param);
                            if (param.getDeclarator(Declarator.ARRAY) != null) {
                                generator.emit(Instruction.ALOAD, "" + idx);
                            } else {
                                generator.emit(Instruction.ILOAD, "" + idx);
                            }
                        } else {
                            int v = (int) objVal;
                            generator.emit(Instruction.SIPUSH, "" + v);
                        }

                        count++;
                    }
                    if (!reEntry) {
                        Generate generate = GenerateFactory.getGenerateFactory().getGenerate(func);
                        ProgramGenerator.getInstance().setInstructionBuffered(true);
                        generate.generate(func);
                        symbol = (Symbol) root.getChildren().get(0).getAttribute(NodeKey.SYMBOL);
                        emitReturnInstruction(symbol);
                        ProgramGenerator.getInstance().emitDirective(Directive.END_METHOD);
                        ProgramGenerator.getInstance().setInstructionBuffered(false);
                        ProgramGenerator.getInstance().popFuncName();
                    }
                    compileFunctionCall(funcName);

                    Object returnVal = func.getAttribute(NodeKey.VALUE);
                    if (returnVal != null) {
                        System.out.println("function call with name " + funcName + " has return value that is " + returnVal.toString());
                        root.setAttribute(NodeKey.VALUE, returnVal);
                    }
                } else {
                    ClibCall libCall = ClibCall.getInstance();
                    if (libCall.isApiCall(funcName)) {
                        Object obj = libCall.invokeApi(funcName);
                        root.setAttribute(NodeKey.VALUE, obj);
                    }
                }

                break;

            case SyntaxProductionInit.Unary_StructOP_Name_TO_Unary:
                child = root.getChildren().get(0);
                String fieldName = (String) root.getAttribute(NodeKey.TEXT);
                Object object = child.getAttribute(NodeKey.SYMBOL);
                boolean isStructArray = false;

                if (object instanceof ArrayValueSetter) {
                    symbol = getStructSymbolFromStructArray(object);
                    symbol.addValueSetter(object);
                    isStructArray = true;
                } else {
                    symbol = (Symbol) child.getAttribute(NodeKey.SYMBOL);
                }

                if (isStructArray) {
                    ArrayValueSetter vs = (ArrayValueSetter) object;
                    Symbol structArray = vs.getSymbol();
                    structArray.addScope(ProgramGenerator.getInstance().getCurrentFuncName());
                } else {
                    symbol.addScope(ProgramGenerator.getInstance().getCurrentFuncName());
                }

                ProgramGenerator.getInstance().putStructToClassDeclaration(symbol);

                if (isSymbolStructPointer(symbol)) {
                    copyBetweenStructAndMem(symbol, false);
                }

                Symbol args = symbol.getArgList();
                while (args != null) {
                    if (args.getName().equals(fieldName)) {
                        args.setStructParent(symbol);
                        break;
                    }

                    args = args.getNextSymbol();
                }

                if (args == null) {
                    System.err.println("access a filed not in struct object!");
                    System.exit(1);
                }

                if (args.getValue() != null) {
                    ProgramGenerator.getInstance().readValueFromStructMember(symbol, args);
                }

                root.setAttribute(NodeKey.SYMBOL, args);
                root.setAttribute(NodeKey.VALUE, args.getValue());

                if (isSymbolStructPointer(symbol)) {
                    checkValidPointer(symbol);
                    structObjSymbol = symbol;
                    monitorSymbol = args;

                    GenerateBrocasterImpl.getInstance().registerReceiverForAfterExe(this);
                } else {
                    structObjSymbol = null;
                }
                break;

            default:
                break;

        }

        return root;
    }

    private Symbol getStructSymbolFromStructArray(Object object) {
        ArrayValueSetter vs = (ArrayValueSetter) object;
        Symbol symbol = vs.getSymbol();
        int idx = (Integer) vs.getIndex();
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if (declarator == null) {
            return null;
        }

        ProgramGenerator.getInstance().createStructArray(symbol);

        Symbol struct = null;
        try {
            struct = (Symbol) declarator.getElement(idx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (struct == null) {
            struct = symbol.copy();
            try {
                declarator.addElement(idx, (Object) struct);
                ProgramGenerator.getInstance().createInstanceForStructArray(symbol, idx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return struct;
    }

    private void emitReturnInstruction(Symbol symbol) {
        if (symbol.hasType(Specifier.INT)) {
            ProgramGenerator.getInstance().emit(Instruction.IRETURN);
        } else {
            ProgramGenerator.getInstance().emit(Instruction.RETURN);
        }
    }

    private void compileFunctionCall(String funcName) {
        ProgramGenerator generator = ProgramGenerator.getInstance();
        String declaration = generator.getDeclarationByName(funcName);
        String call = generator.getProgramName() + "/" + declaration;
        generator.emit(Instruction.INVOKESTATIC, call);
    }

    private void setPointerValue(AstNode root, Symbol symbol, int index) {
        MemoryHeap memHeap = MemoryHeap.getInstance();
        int addr = (Integer) symbol.getValue();
        Map.Entry<Integer, byte[]> entry = memHeap.getMem(addr);
        byte[] content = entry.getValue();
        if (symbol.getByteSize() == 1) {
            root.setAttribute(NodeKey.VALUE, content[index]);
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.put(content, index, 4);
            buffer.flip();
            int v = buffer.getInt();
            root.setAttribute(NodeKey.VALUE, v);
        }
    }

    private boolean isSymbolStructPointer(Symbol symbol) {
        if (symbol.getDeclarator(Declarator.POINTER) != null && symbol.getArgList() != null) {
            return true;
        }

        return false;
    }

    private void checkValidPointer(Symbol symbol) {
        if (symbol.getDeclarator(Declarator.POINTER) != null && symbol.getValue() == null) {
            System.err.println("Aceess Empty Pointer");
            System.exit(1);
        }
    }

    @Override
    public void handleExecutorMessage(AstNode code) {
        int productNum = (Integer) code.getAttribute(NodeKey.PRODUCTION);
        if (productNum != SyntaxProductionInit.NoCommaExpr_Equal_NoCommaExpr_TO_NoCommaExpr) {
            return;
        }

        Object object = code.getAttribute(NodeKey.SYMBOL);

        if (object == null) {
            return;
        }

        Symbol symbol;
        if (object instanceof ValueSetter) {
            symbol = ((ValueSetter) object).getSymbol();
        } else {
            symbol = (Symbol) object;
        }


        if (symbol == monitorSymbol) {
            System.out.println("UnaryNodeGenerate receive msg for assign execution");
            copyBetweenStructAndMem(structObjSymbol, true);
        }
    }

    private void copyBetweenStructAndMem(Symbol symbol, boolean isFromStructToMem) {
        Integer addr = (Integer) symbol.getValue();
        MemoryHeap memHeap = MemoryHeap.getInstance();
        Map.Entry<Integer, byte[]> entry = memHeap.getMem(addr);
        byte[] mems = entry.getValue();
        Stack<Symbol> stack = reverseStructSymbolList(symbol);
        int offset = 0;

        while (stack.empty() != true) {
            Symbol sym = stack.pop();

            try {
                if (isFromStructToMem) {
                    offset += writeStructVariablesToMem(sym, mems, offset);
                } else {
                    offset += writeMemToStructVariables(sym, mems, offset);
                }

            } catch (Exception e) {
                System.err.println("error when copyin struct variables to memory");
                e.printStackTrace();
            }
        }
    }

    private int writeStructVariablesToMem(Symbol symbol, byte[] mem, int offset) throws Exception {
        if (symbol.getArgList() != null) {
            return writeStructVariablesToMem(symbol, mem, offset);
        }

        int sz = symbol.getByteSize();
        if (symbol.getValue() == null && symbol.getDeclarator(Declarator.ARRAY) == null) {
            return sz;
        }

        if (symbol.getDeclarator(Declarator.ARRAY) == null) {
            Integer val = (Integer) symbol.getValue();
            byte[] bytes = ByteBuffer.allocate(4).putInt(val).array();
            for (int i = 3; i >= 4 - sz; i--) {
                mem[offset + 3 - i] = bytes[i];
            }

            return sz;
        } else {
            return copyArrayVariableToMem(symbol, mem, offset);
        }
    }

    private int writeMemToStructVariables(Symbol symbol, byte[] mem, int offset) throws Exception {
        if (symbol.getArgList() != null) {
            return writeMemToStructVariables(symbol, mem, offset);
        }

        int sz = symbol.getByteSize();
        int val = 0;
        if (symbol.getDeclarator(Declarator.ARRAY) == null) {
            val = fromByteArrayToInteger(mem, offset, sz);
            symbol.setValue(val);
        } else {
            return copyMemToArrayVariable(symbol, mem, offset);
        }

        return sz;
    }

    private int fromByteArrayToInteger(byte[] mem, int offset, int sz) {
        int val = 0;
        switch (sz) {
            case 1:
                val = mem[offset];
                break;
            case 2:
                val = (mem[offset + 1] << 8 | mem[offset]);
                break;
            case 4:
                val = (mem[offset + 3] << 24 | mem[offset + 2] << 16 | mem[offset + 1] << 8 |
                        mem[offset]);
                break;
        }

        return val;
    }

    private int copyMemToArrayVariable(Symbol symbol, byte[] mem, int offset) {
        int sz = symbol.getByteSize();
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if (declarator == null) {
            return 0;
        }

        int size = 0;
        int elemCount = declarator.getElementNum();
        for (int i = 0; i < elemCount; i++) {
            int val = fromByteArrayToInteger(mem, offset + size, sz);
            size += sz;
            try {
                declarator.addElement(i, val);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return size;
    }

    private int copyArrayVariableToMem(Symbol symbol, byte[] mem, int offset) {
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if (declarator == null) {
            return 0;
        }

        int sz = symbol.getByteSize();
        int elemCount = declarator.getElementNum();
        for (int i = 0; i < elemCount; i++) {
            try {
                Integer val = (Integer) declarator.getElement(i);
                byte[] bytes = ByteBuffer.allocate(sz).putInt(val).array();
                for (int j = 0; j < sz; j++) {
                    mem[offset + j] = bytes[j];
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return sz * elemCount;
    }

    private Stack<Symbol> reverseStructSymbolList(Symbol symbol) {
        Stack<Symbol> stack = new Stack<Symbol>();
        Symbol sym = symbol.getArgList();
        while (sym != null) {
            stack.push(sym);
            sym = sym.getNextSymbol();
        }

        return stack;
    }

}
