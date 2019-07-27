package codegen;

import ast.AstNode;
import ast.NodeKey;

/**
 * @author dejavudwh isHudw
 */

public abstract class BaseTraverse implements Traverse {
    GeneratorBrocaster generatorBrocaster;
    Generator generator;

    private static boolean continueExecute = true;

    public BaseTraverse() {
        generatorBrocaster = GeneratorBrocasterImpl.getInstance();
        generator = Generator.getInstance();
    }

    protected void traverseChildren(AstNode root) {
        TraverseFactory factory = TraverseFactory.getInstance();
        root.reverseChildren();

        int i = 0;
        while (i < root.getChildren().size()) {

            if (!continueExecute) {
                break;
            }

            AstNode child = root.getChildren().get(i);

            generatorBrocaster.brocastBeforeExecution(child);

            Traverse traverse = factory.getTraverse(child);
            if (traverse != null) {
                traverse.traverse(child);
            } else {
                System.err.println("Not suitable Traverse found, node is: " + child.toString());
            }

            generatorBrocaster.brocastAfterExecution(child);

            i++;
        }
    }


    protected void copyChild(AstNode root, AstNode child) {
        root.setAttribute(NodeKey.SYMBOL, child.getAttribute(NodeKey.SYMBOL));
        root.setAttribute(NodeKey.VALUE, child.getAttribute(NodeKey.VALUE));
        root.setAttribute(NodeKey.TEXT, child.getAttribute(NodeKey.TEXT));
    }

    protected AstNode traverseChild(AstNode root, int childIdx) {
        root.reverseChildren();
        AstNode child;
        TraverseFactory factory = TraverseFactory.getInstance();
        child = (AstNode) root.getChildren().get(childIdx);
        Traverse traverse = factory.getTraverse(child);
        AstNode res = (AstNode) traverse.traverse(child);

        return res;
    }
}
