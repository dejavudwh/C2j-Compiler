package interpreter;

import debug.ConsoleDebugColor;
import symboltable.Declarator;
import symboltable.Symbol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ClibCall {
    private Set<String> apiSet;

    private ClibCall() {
        apiSet = new HashSet<String>();
        apiSet.add("printf");
        apiSet.add("malloc");
        apiSet.add("sizeof");
    }

    private static ClibCall instance = null;

    public static ClibCall getInstance() {

        if (instance == null) {
            instance = new ClibCall();
        }

        return instance;
    }

    public boolean isApiCall(String funcName) {
        return apiSet.contains(funcName);
    }

    public Object invokeApi(String funcName) {
        switch (funcName) {

            case "printf":
                return handlePrintfCall();

            case "malloc":
                return handleMallocCall();

            case "sizeof":
                return handleSizeOfCall();
            default:
                return null;
        }
    }

    private Object handleSizeOfCall() {
        ArrayList<Object> symList = FunctionArgumentList.getInstance().getFuncArgSymsList(false);
        Integer totalSize = calculateVarSize((Symbol) symList.get(0));

        return totalSize;
    }

    private Integer calculateVarSize(Symbol symbol) {
        int size = 0;
        if (symbol.getArgList() == null) {
            size = symbol.getByteSize();
        } else {
            Symbol head = symbol.getArgList();
            while (head != null) {
                size += calculateVarSize(head);
                head = head.getNextSymbol();
            }
        }

        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if (declarator != null) {
            size = size * declarator.getElementNum();
        }

        return size;
    }

    private Object handleMallocCall() {
        ArrayList<Object> argsList = FunctionArgumentList.getInstance().getFuncArgList(false);
        int size = (Integer) argsList.get(0);
        int addr = 0;

        if (size > 0) {
            MemoryHeap memHeap = MemoryHeap.getInstance();
            addr = memHeap.allocMem(size);
        }

        return addr;

    }

    private Object handlePrintfCall() {
        ArrayList<Object> argsList = FunctionArgumentList.getInstance().getFuncArgList(false);
        ArrayList<Object> argsSymList = FunctionArgumentList.getInstance().getFuncArgSymsList(false);
        String argStr = (String) argsList.get(0);
        String formatStr = "";

        int i = 0;
        int argCount = 1;
        while (i < argStr.length()) {
            if (argStr.charAt(i) == '%' && i + 1 < argStr.length() &&
                    argStr.charAt(i + 1) == 'd') {
                i += 2;
                formatStr += argsList.get(argCount);
                argCount++;
            } else {
                formatStr += argStr.charAt(i);
                i++;
            }
        }

        System.out.println(formatStr);

        return null;
    }
}
