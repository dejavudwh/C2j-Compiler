package interpreter;

import ast.AstNode;

/**
 *
 * @author dejavudwh isHudw
 */

public class StatementListExecutor extends BaseExecutor{
    @Override
    public Object execute(AstNode root) {
        executeChildren(root);
        copyChild(root, root.getChildren().get(0));
        return root;
    }
}
