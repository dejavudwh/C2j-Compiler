package interpreter;

import ast.AstNode;
import ast.NodeKey;

import javax.xml.soap.Node;

/**
 * @author dejavudwh isHudw
 */

public class ElseStatementExecutor extends BaseExecutor {

    @Override
    public Object execute(AstNode root) {
        AstNode res = executeChild(root, 0);
        Object obj = res.getAttribute(NodeKey.VALUE);
        if ((Integer) obj == 0) {
            res = executeChild(root, 1);
        }

        copyChild(root, res);

        return root;
    }

}
