package interpreter;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

/**
 * @author dejavudwh isHudw
 */

public class StatementExecutor extends BaseExecutor {
    private enum LoopType {
        /**
         * loop tag
         */
        FOR,
        WHILE,
        DO_WHILE
    }

    ;

    @Override
    public Object execute(AstNode root) {
        int production = (int) root.getAttribute(NodeKey.PRODUCTION);
        AstNode node;

        switch (production) {
            case SyntaxProductionInit.FOR_OptExpr_Test_EndOptExpr_Statement_TO_Statement:
                executeChild(root, 0);

                while (isLoopContinute(root, LoopType.FOR)) {
                    //execute statement in for body
                    executeChild(root, 3);
                    //execute EndOptExpr
                    executeChild(root, 2);
                }
                break;

            case SyntaxProductionInit.While_LP_Test_Rp_TO_Statement:
                while (isLoopContinute(root, LoopType.WHILE)) {
                    executeChild(root, 1);
                }
                break;

            case SyntaxProductionInit.Do_Statement_While_Test_To_Statement:
                do {
                    executeChild(root, 0);
                } while (isLoopContinute(root, LoopType.DO_WHILE));

                break;

            case SyntaxProductionInit.Return_Semi_TO_Statement:
                isContinueExecution(false);

                break;

            case SyntaxProductionInit.Return_Expr_Semi_TO_Statement:
                node = executeChild(root, 0);
                Object obj = node.getAttribute(NodeKey.VALUE);
                setReturnObj(obj);
                isContinueExecution(false);

                break;

            default:
                executeChildren(root);

                break;
        }

        return root;
    }

    private boolean isLoopContinute(AstNode root, LoopType type) {
        AstNode res = null;
        if (type == LoopType.FOR || type == LoopType.DO_WHILE) {
            res = executeChild(root, 1);
        } else if (type == LoopType.WHILE) {
            res = executeChild(root, 0);
        }

        int result = (Integer) res.getAttribute(NodeKey.VALUE);
        return res != null && result != 0;

    }
}
