package interpreter;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

import java.util.ArrayList;

/**
 * @author deajvudhw isHudw
 */

public class ArgsExecutor extends BaseExecutor {

    @Override
    public Object execute(AstNode root) {
        int production = (Integer) root.getAttribute(NodeKey.PRODUCTION);
        ArrayList<Object> argList = new ArrayList<>();
        ArrayList<Object> symList = new ArrayList<>();
        AstNode child;
        switch (production) {
            case SyntaxProductionInit.NoCommaExpr_TO_Args:
                child = (AstNode) executeChild(root, 0);
                Object objVal = child.getAttribute(NodeKey.VALUE);
                argList.add(objVal);
                objVal = child.getAttribute(NodeKey.SYMBOL);
                symList.add(objVal);
                break;

            case SyntaxProductionInit.NoCommaExpr_Comma_Args_TO_Args:
                child = executeChild(root, 0);
                objVal = child.getAttribute(NodeKey.VALUE);
                argList.add(objVal);
                objVal = child.getAttribute(NodeKey.SYMBOL);
                symList.add(objVal);

                child = (AstNode) executeChild(root, 1);
                ArrayList<Object> list = (ArrayList<Object>) child.getAttribute(NodeKey.VALUE);
                argList.addAll(list);
                list = (ArrayList<Object>) child.getAttribute(NodeKey.SYMBOL);
                symList.add(list);
                break;

            default:
                break;
        }

        root.setAttribute(NodeKey.VALUE, argList);
        root.setAttribute(NodeKey.SYMBOL, symList);
        return root;
    }

}