package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.BaseTraverse;

/**
 * @author dejavdwh isHudw
 */

public class IfStatementTraverse extends BaseTraverse {

    @Override
    public Object traverse(AstNode root) {
        Generator generator = Generator.getInstance();
        AstNode res = traverseChild(root, 0);

        String curBranch = generator.getCurrentBranch();
        generator.emitComparingCommand();

        Integer val = (Integer) res.getAttribute(NodeKey.VALUE);
        copyChild(root, res);
        if ((val != null && val != 0) || BaseTraverse.isCompileMode) {
            generator.increaseIfElseEmbed();
            boolean b = BaseTraverse.inIfElseStatement;
            BaseTraverse.inIfElseStatement = false;
            traverseChild(root, 1);
            BaseTraverse.inIfElseStatement = b;
            generator.decreaseIfElseEmbed();
        }

        if (BaseTraverse.inIfElseStatement) {
            String branchOut = generator.getBranchOut();
            generator.emitString(Instruction.GOTO.toString() + " " + branchOut);
        } else {
            generator.emitString(curBranch + ":\n");
            generator.increaseBranch();
        }

        return root;
    }

}
