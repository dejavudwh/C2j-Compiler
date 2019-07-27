package codegen;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

/**
 *
 * @author dejavudhw isHudw
 */

public class OptExprTraverse extends BaseTraverse {

    @Override
    public Object traverse(AstNode root) {
        int production = (int)root.getAttribute(NodeKey.PRODUCTION);
        switch (production) {
            case SyntaxProductionInit.Semi_TO_OptExpr:
                return root;

            case SyntaxProductionInit.Expr_Semi_TO_OptExpr:
                return traverseChild(root, 0);

            default:
                return root;
        }
    }
}
