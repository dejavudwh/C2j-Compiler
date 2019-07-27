package codegen;

import ast.AstNode;

/**
 *
 * @author dejavudwh isHudw
 */

public class CompoundStmtTraverse extends BaseTraverse {

    @Override
    public Object traverse(AstNode root) {
        return traverseChild(root, 0);
    }
}
