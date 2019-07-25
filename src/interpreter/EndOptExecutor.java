package interpreter;

import ast.AstNode;
import interpreter.BaseExecutor;

/**
 * Increase the count at the end of the for loop
 * @author dejavudwh isHudw
 */

public class EndOptExecutor extends BaseExecutor {
    @Override
    public Object execute(AstNode root) {
        return executeChild(root, 0);
    }
}
