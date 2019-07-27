package codegen;

import ast.AstNode;

/**
 *
 * @author dejavudwh isHudw
 */

public class TestTraverse extends BaseTraverse {
    @Override
    public Object traverse(AstNode root) {
        traverseChildren(root);
        copyChild(root, root.getChildren().get(0));

        return root;
    }
}
