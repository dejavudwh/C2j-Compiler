package interpreter;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

/**
 * @author dejavudwh isHudw
 */

public class ExprExecutor extends BaseExecutor {

    @Override
    public Object execute(AstNode root) {
        executeChildren(root);
        int production = (int) root.getAttribute(NodeKey.PRODUCTION);

        switch (production) {
            case SyntaxProductionInit.NoCommaExpr_TO_Expr:
                copyChild(root, root.getChildren().get(0));
                break;

            default:
                break;
        }

        return root;
    }
}
