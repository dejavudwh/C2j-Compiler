package interpreter;

import ast.AstNode;

/**
 *
 * @author dejavudwh isHudw
 */

public interface Executor {
    /**
     * The execution function for each node
     * @param root ast node
     * @return Object
    */
    Object execute(AstNode root);
}
