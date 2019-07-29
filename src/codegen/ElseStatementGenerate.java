package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.backend.ProgramGenerator;

public class ElseStatementGenerate extends BaseGenerate {
    private ProgramGenerator generator = ProgramGenerator.getInstance();

    @Override
    public Object generate(AstNode root) {
        BaseGenerate.inIfElseStatement = true;

        String branch = generator.getCurrentBranch();
        AstNode res = generateChild(root, 0);

        BaseGenerate.inIfElseStatement = false;

        branch = "\n" + branch + ":\n";
        generator.emitString(branch);

        generator.increaseBranch();

        Object obj = res.getAttribute(NodeKey.VALUE);
        generator.incraseIfElseEmbed();
        res = generateChild(root, 1);
        generator.decraseIfElseEmbed();
        
        copyChild(root, res);

        generator.emitBranchOut();

        return root;
    }

}
