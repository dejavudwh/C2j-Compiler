package codegen;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;
import symboltable.Symbol;
import symboltable.TypeSystem;

import java.util.ArrayList;

/**
 * @author dejavudwh isHudw
 */

public class ExtDefTraverse extends BaseTraverse {
    private ArrayList<Object> argsList = new ArrayList<>();
    AstNode root;
    String funcName;

    @Override
    public Object traverse(AstNode root) {
        this.root = root;
        int production = (Integer) root.getAttribute(NodeKey.PRODUCTION);
        switch (production) {
            case SyntaxProductionInit.OptSpecifiers_FunctDecl_CompoundStmt_TO_ExtDef:
                AstNode child = root.getChildren().get(0);
                funcName = (String) child.getAttribute(NodeKey.TEXT);
                root.setAttribute(NodeKey.TEXT, funcName);
                saveArgs();
                traverseChild(root, 0);

                traverseChild(root, 1);
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
        System.out.println("Save arguments....");
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
        System.out.println("Restore arguments....");
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
