package codegen;

import ast.AstNode;

/**
 *
 * @author dejavdwh isHudw
 */

public class EndOptTraverse extends BaseTraverse {

    @Override
    public Object traverse(AstNode root) {
        return traverseChild(root, 0);
    }
}
