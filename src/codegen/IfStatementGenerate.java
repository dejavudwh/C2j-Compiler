package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.backend.Instruction;
import codegen.backend.ProgramGenerator;

/**
 * @author dejavudwhisHudw
 */

public class IfStatementGenerate extends BaseGenerate {

    @Override
    public Object generate(AstNode root) {
        ProgramGenerator generator = ProgramGenerator.getInstance();
        AstNode res = generateChild(root, 0);

        String curBranch = generator.getCurrentBranch();
        generator.emitComparingCommand();

        Integer val = (Integer) res.getAttribute(NodeKey.VALUE);
        copyChild(root, res);

        generator.incraseIfElseEmbed();
        boolean b = BaseGenerate.inIfElseStatement;
        BaseGenerate.inIfElseStatement = false;
        generateChild(root, 1);
        BaseGenerate.inIfElseStatement = b;
        generator.decraseIfElseEmbed();

        if (BaseGenerate.inIfElseStatement) {
            String branchOut = generator.getBranchOut();
            generator.emitString(Instruction.GOTO.toString() + " " + branchOut);
        } else {
            generator.emitString(curBranch + ":\n");
            generator.increaseBranch();
        }

        return root;
    }

}
