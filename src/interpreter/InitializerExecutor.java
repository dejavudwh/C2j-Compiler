package interpreter;

import ast.AstNode;

/**
 *
 * @author dejavudwh isHudw
 */

public class InitializerExecutor extends BaseExecutor {

    @Override
    public Object execute(AstNode root) {
        executeChild(root, 0);
        copyChild(root, root.getChildren().get(0));

        return root;
    }

}
