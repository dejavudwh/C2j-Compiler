package codegen;

import java.util.ArrayList;

import ast.AstNode;
import ast.NodeKey;
import debug.ConsoleDebugColor;
import interpreter.ValueSetter;
import parse.SyntaxProductionInit;
import symboltable.*;
import symboltable.TypeSystem;

public class ExtDefGenerate extends BaseGenerate {
    private ArrayList<Object> argsList = new ArrayList<Object>();
    AstNode root;
    String funcName;

    @Override
    public Object generate(AstNode root) {
        this.root = root;
        int production = (Integer) root.getAttribute(NodeKey.PRODUCTION);
        switch (production) {
            case SyntaxProductionInit.OptSpecifiers_FunctDecl_CompoundStmt_TO_ExtDef:
                AstNode child = root.getChildren().get(0);
                funcName = (String) child.getAttribute(NodeKey.TEXT);
                root.setAttribute(NodeKey.TEXT, funcName);
                saveArgs();
                generateChild(root, 0);
                generateChild(root, 1);
                Object returnVal = getReturnObj();
                clearReturnObj();

                if (returnVal != null) {
                    root.setAttribute(NodeKey.VALUE, returnVal);
                }

                isContinueExecution(true);

                restoreArgs();

                break;

            default:
                break;
        }
        return root;
    }

    private void saveArgs() {
        ConsoleDebugColor.outlnPurple("Save arguments....");
        TypeSystem typeSystem = TypeSystem.getInstance();
        ArrayList<Symbol> args = typeSystem.getSymbolsByScope(funcName);
        int count = 0;
        while (count < args.size()) {
            Symbol arg = args.get(count);
            Object value = arg.getValue();
            argsList.add(value);
            count++;
        }
    }

    private void restoreArgs() {
        ConsoleDebugColor.outlnPurple("Restore arguments....");
        TypeSystem typeSystem = TypeSystem.getInstance();
        ArrayList<Symbol> args = typeSystem.getSymbolsByScope(funcName);
        int count = 0;

        while (args != null && count < argsList.size()) {
            ValueSetter setter = (ValueSetter) args.get(count);
            try {
                Object value = argsList.get(count);
                setter.setValue(value);
            } catch (Exception e) {
                e.printStackTrace();
            }

            count++;
        }
    }

}
