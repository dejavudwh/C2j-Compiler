package codegen;

import ast.AstNode;
import interpreter.*;

/**
 *
 * @author dejavudwh isHudw
 */

public class TraverseFactory {
    private static TraverseFactory traverseFactory = null;
    private TraverseFactory() {

    }

    public static TraverseFactory getInstance() {
        if (traverseFactory == null) {
            traverseFactory = new TraverseFactory();
        }

        return traverseFactory;
    }

    public Traverse getTraverse(AstNode node) {
        return null;
    }
}
