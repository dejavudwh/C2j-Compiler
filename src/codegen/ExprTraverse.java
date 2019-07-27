package codegen;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

/**
 * @author dejavudwh isHudw
 */

public class ExprTraverse extends BaseTraverse {

    @Override
    public Object traverse(AstNode root) {
        traverseChildren(root);
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
