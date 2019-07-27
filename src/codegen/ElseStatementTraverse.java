package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.BaseTraverse;

/**
 * @author dejavudwh isHudw
 */

public class ElseStatementTraverse extends BaseTraverse {
    private Generator generator = Generator.getInstance();

    @Override
    public Object traverse(AstNode root) {
        BaseTraverse.inIfElseStatement = true;

        /* To traverse the if first */
        String branch = generator.getCurrentBranch();
        AstNode res = traverseChild(root, 0);

        BaseTraverse.inIfElseStatement = false;

        branch = "\n" + branch + ":\n";
        generator.emitString(branch);

        generator.increaseBranch();

        Object obj = res.getAttribute(NodeKey.VALUE);
        if ((Integer) obj == 0 || BaseTraverse.isCompileMode) {
            generator.increaseIfElseEmbed();
            /* If block does not execute, else block executes */
            res = traverseChild(root, 1);
            generator.decreaseIfElseEmbed();
        }

        copyChild(root, res);

        generator.emitBranchOut();

        return root;
    }
}
