package interpreter;

import ast.AstNode;
import ast.NodeKey;

/**
 * If_Test_Statement_TO_IFStatement
 * @author dejavudwh isHudw
 */

public class IfStatementExecutor extends BaseExecutor {

    @Override
    public Object execute(AstNode root) {

        AstNode res = executeChild(root, 0);
        Integer val = (Integer)res.getAttribute(NodeKey.VALUE);
        copyChild(root, res);

        if (val != null && val != 0) {
            executeChild(root, 1);
        }

        return root;
    }

}
