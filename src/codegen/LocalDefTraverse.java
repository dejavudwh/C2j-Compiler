package codegen;

import ast.AstNode;

/**
 *
 * @author dejavudhw isHudw
 */

public class LocalDefTraverse extends BaseTraverse {

    @Override
    public Object traverse(AstNode root) {
        traverseChild(root, 0);

        return root;
    }
}
