package codegen;

import ast.AstNode;

/**
 *
 * @author dejavdwh isHudw
 */

public class InitializerTraverse extends BaseTraverse {

    @Override
    public Object traverse(AstNode root) {
        traverseChild(root, 0);
        copyChild(root, root.getChildren().get(0));

        return root;
    }
}
