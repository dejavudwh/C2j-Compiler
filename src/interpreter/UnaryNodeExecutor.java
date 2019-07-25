package interpreter;

import ast.AstBuilder;
import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;
import symboltable.Declarator;
import symboltable.Symbol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

/**
 * @author dejavudwh isHudw
 */

public class UnaryNodeExecutor extends BaseExecutor implements ExecutorReceiver {
    private Symbol structObjSymbol = null;
    private Symbol monitorSymbol = null;

    @Override
    public Object execute(AstNode root) {
        executeChildren(root);

        int production = (Integer) root.getAttribute(NodeKey.PRODUCTION);
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
                int index = (Integer) child.getAttribute(NodeKey.VALUE);

                try {
                    Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
                    if (declarator != null) {
                        Object val = declarator.getElement(index);
                        root.setAttribute(NodeKey.VALUE, val);
                        ArrayValueSetter setter = new ArrayValueSetter(symbol, index);
                        root.setAttribute(NodeKey.SYMBOL, setter);
                        root.setAttribute(NodeKey.TEXT, symbol.getName());
                    }
                    Declarator pointer = symbol.getDeclarator(Declarator.POINTER);
                    if (pointer != null) {
                        setPointerValue(root, symbol, index);
                        //create a PointerSetter
                        PointerValueSetter pv = new PointerValueSetter(symbol, index);
                        root.setAttribute(NodeKey.SYMBOL, pv);
                        root.setAttribute(NodeKey.TEXT, symbol.getName());
                    }

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
                break;

            case SyntaxProductionInit.Unary_Incop_TO_Unary:
            case SyntaxProductionInit.Unary_DecOp_TO_Unary:
                symbol = (Symbol) root.getChildren().get(0).getAttribute(NodeKey.SYMBOL);
                Integer val = (Integer) symbol.getValue();
                ValueSetter setter;
                setter = (ValueSetter) symbol;
                try {
                    if (production == SyntaxProductionInit.Unary_Incop_TO_Unary) {
                        setter.setValue(val + 1);
                    } else {
                        setter.setValue(val - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Runtime Error: Assign Value Error");
                }
                break;

            case SyntaxProductionInit.LP_Expr_RP_TO_Unary:
                child = root.getChildren().get(0);
                copyChild(root, child);
                break;

            case SyntaxProductionInit.Start_Unary_TO_Unary:
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
                String funcName = (String) root.getChildren().get(0).getAttribute(NodeKey.TEXT);
                if (production == SyntaxProductionInit.Unary_LP_ARGS_RP_TO_Unary) {
                    AstNode argsNode = root.getChildren().get(1);
                    ArrayList<Object> argList = (ArrayList<Object>) argsNode.getAttribute(NodeKey.VALUE);
                    ArrayList<Object> symList = (ArrayList<Object>) argsNode.getAttribute(NodeKey.SYMBOL);
                    FunctionArgumentList.getInstance().setFuncArgList(argList);
                    FunctionArgumentList.getInstance().setFuncArgSymbolList(symList);
                }

                AstNode func = AstBuilder.getInstance().getFunctionNodeByName(funcName);
                if (func != null) {
                    Executor executor = ExecutorFactory.getInstance().getExecutor(func);
                    executor.execute(func);
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
                symbol = (Symbol) child.getAttribute(NodeKey.SYMBOL);

                if (isSymbolStructPointer(symbol)) {
                    copyBetweenStructAndMem(symbol, false);
                }

                Symbol args = symbol.getArgList();
                while (args != null) {
                    if (args.getName().equals(fieldName)) {
                        break;
                    }

                    args = args.getNextSymbol();
                }

                if (args == null) {
                    System.err.println("access a filed not in struct object!");
                    System.exit(1);
                }

                root.setAttribute(NodeKey.SYMBOL, args);
                root.setAttribute(NodeKey.VALUE, args.getValue());

                if (isSymbolStructPointer(symbol)) {
                    checkValidPointer(symbol);
                    structObjSymbol = symbol;
                    monitorSymbol = args;

                    ExecutorBrocasterImpl.getInstance().registerReceiverForAfterExe(this);
                } else {
                    structObjSymbol = null;
                }
                break;

            default:
                break;
        }

        return root;
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
        return (symbol.getDeclarator(Declarator.POINTER) != null && symbol.getArgList() != null);
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
            System.out.println("UnaryNodeExecutor receive msg for assign execution");
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

        while (!stack.empty()) {
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

    private int writeStructVariablesToMem(Symbol symbol, byte[] mem, int offset) {
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

    private int writeMemToStructVariables(Symbol symbol, byte[] mem, int offset) {
        if (symbol.getArgList() != null) {
            //struct variable, copy mem to struct recursively
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
            default:
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
        Stack<Symbol> stack = new Stack<>();
        Symbol sym = symbol.getArgList();
        while (sym != null) {
            stack.push(sym);
            sym = sym.getNextSymbol();
        }

        return stack;
    }

}
