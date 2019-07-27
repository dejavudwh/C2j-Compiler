package codegen;

import ast.AstNode;

/**
 *
 * @author dejavudwh isHudw
 */

public class TraverseGenerator implements Traverse {
    private static TraverseGenerator instance = null;
    public static TraverseGenerator getInstance() {
        if (instance == null) {
            instance = new TraverseGenerator();
        }

        return instance;
    }

    private TraverseGenerator() {

    }

    @Override
    public Object traverse(AstNode root) {
        if (root == null) {
            return null;
        }

        TraverseFactory factory = TraverseFactory.getInstance();
        Traverse traverse = factory.getTraverse(root);
        traverse.traverse(root);

        return root;
    }
}
