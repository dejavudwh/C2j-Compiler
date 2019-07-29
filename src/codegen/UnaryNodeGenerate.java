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


public class UnaryNodeGenerate extends BaseGenerate implements GenerateReceiver {
    private Symbol structObjSymbol = null;
    private Symbol monitorSymbol = null;

    @Override
    public Object generate(AstNode root) {

        int production = (Integer) root.getAttribute(NodeKey.PRODUCTION);
        executeChildren(root);

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
                    root.setAttribute(NodeKey.VALUE, Float.valueOf(text));
                } else {
                    value = Integer.valueOf(text);
                    root.setAttribute(NodeKey.VALUE, Integer.valueOf(text));
                }
                break;

            case SyntaxProductionInit.Name_TO_Unary:
                symbol = (Symbol) root.getAttribute(NodeKey.SYMBOL);
                if (symbol != null) {
                    root.setAttribute(NodeKey.VALUE, symbol.getValue());
                    root.setAttribute(NodeKey.TEXT, symbol.getName());

                    AstNode func = AstBuilder.getInstance().getFunctionNodeByName(symbol.getName());

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
                //change here check null before convert to integer
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
                        ArrayValueSetter setter = null;
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
                //change here
                int i = generator.getLocalVariableIndex(symbol);
                generator.emit(Instruction.ILOAD, "" + i);
                generator.emit(Instruction.SIPUSH, "" + 1);

                try {
                    if (production == SyntaxProductionInit.Unary_Incop_TO_Unary) {
                        if (BaseGenerate.isCompileMode == false) {
                            setter.setValue(val + 1);
                        }
                        generator.emit(Instruction.IADD);
                    } else {
                        if (BaseGenerate.isCompileMode == false) {
                            setter.setValue(val - 1);
                        }
                        generator.emit(Instruction.ISUB);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
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
                child = root.getChildren().get(0);
                int addr = (Integer) child.getAttribute(NodeKey.VALUE); //get mem addr
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
                //先获得函数名
                boolean reEntry = false;
                String funcName = (String) root.getChildren().get(0).getAttribute(NodeKey.TEXT);
                //change here
                /*
                 * 如果函数名被记录过，那表明现在的函数调用其实是递归调用
                 */
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

                //找到函数执行树头节点
                AstNode func = AstBuilder.getInstance().getFunctionNodeByName(funcName);
                if (func != null) {
                    //change here push parameters before calling function
                    /*
                     * 函数调用时，把当前被调用的函数名记录下来，如果函数体内发送递归调用，那么编译器还会再次进入到
                     * 这里，如果进入时判断到函数名跟我们这里存储的函数名一致，那表明发生了递归调用。
                     */
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
                    //problem here handle reentry
                    if (BaseGenerate.isCompileMode == true && reEntry == false) {
                        /*
                         * 在编译状态下，遇到函数自我递归调用则不需要再次为函数生成代码，只需要生成invoke指令即可
                         */
                        Generate generate = GenerateFactory.getGenerateFactory().getExecutor(func);
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
                    if (libCall.isAPICall(funcName)) {
                        Object obj = libCall.invokeAPI(funcName);
                        root.setAttribute(NodeKey.VALUE, obj);
                    }
                }


                break;

            case SyntaxProductionInit.Unary_StructOP_Name_TO_Unary:
                /*
                 * 当编译器读取到myTag.x 这种类型的语句时，会走入到这里
                 */
                child = root.getChildren().get(0);
                String fieldName = (String) root.getAttribute(NodeKey.TEXT);
                Object object = child.getAttribute(NodeKey.SYMBOL);
                boolean isStructArray = false;

                if (object instanceof ArrayValueSetter) {
                    //这里表示正在读取结构体数组,先构造结构体数组
                    symbol = getStructSymbolFromStructArray(object);
                    symbol.addValueSetter(object);
                    isStructArray = true;
                } else {
                    symbol = (Symbol) child.getAttribute(NodeKey.SYMBOL);
                }

                //先把结构体变量的作用范围设置为定义它的函数名
                if (isStructArray == true) {
                    //把结构体数组symbol对象的作用域范围设置为包含它的函数
                    ArrayValueSetter vs = (ArrayValueSetter) object;
                    Symbol structArray = vs.getSymbol();
                    structArray.addScope(ProgramGenerator.getInstance().getCurrentFuncName());
                } else {
                    symbol.addScope(ProgramGenerator.getInstance().getCurrentFuncName());
                }
                //如果是第一次访问结构体成员变量,那么将结构体声明成一个类
                ProgramGenerator.getInstance().putStructToClassDeclaration(symbol);

                if (isSymbolStructPointer(symbol)) {
                    copyBetweenStructAndMem(symbol, false);
                }

                /*
                 * 假设当前解析的语句是myTag.x, 那么args对应的就是变量x
                 * 通过调用setStructParent 把args对应的变量x 跟包含它的结构体变量myTag
                 * 关联起来
                 */
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
                /*
                 * 把读取结构体成员变量转换成对应的java汇编代码，也就是使用getfield指令把对应的成员变量的值读取出来，然后压入堆栈顶部
                 */
                //TODO 需要区分结构体是否来自于结构体数组
                if (args.getValue() != null) {
                    ProgramGenerator.getInstance().readValueFromStructMember(symbol, args);
                }

                root.setAttribute(NodeKey.SYMBOL, args);
                root.setAttribute(NodeKey.VALUE, args.getValue());

                if (isSymbolStructPointer(symbol) == true) {
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (struct == null) {
            struct = symbol.copy();
            try {
                declarator.addElement(idx, (Object) struct);
                //通过指令为数组中的某个下标处创建结构体实例
                ProgramGenerator.getInstance().createInstanceForStructArray(symbol, idx);
            } catch (Exception e) {
                // TODO Auto-generated catch block
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

        Symbol symbol = null;
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
                if (isFromStructToMem == true) {
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
                // TODO Auto-generated catch block
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
