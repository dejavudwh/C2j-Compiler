package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.backend.Instruction;
import codegen.backend.ProgramGenerator;
import parse.SyntaxProductionInit;


public class StatementGenerate extends BaseGenerate {
    private ProgramGenerator generator = ProgramGenerator.getInstance();

    private enum LoopType {
        /* Type of loop statement */
        FOR,
        WHILE,
        DO_WHILE
    }

    ;


    @Override
    public Object generate(AstNode root) {
        int production = (int) root.getAttribute(NodeKey.PRODUCTION);
        AstNode node;

        switch (production) {
            case SyntaxProductionInit.LocalDefs_TO_Statement:
                generateChild(root, 0);
                break;

            case SyntaxProductionInit.FOR_OptExpr_Test_EndOptExpr_Statement_TO_Statement:
                generateChild(root, 0);

                generator.emitLoopBranch();
                String branch = generator.getCurrentBranch();
                isLoopContinue(root, LoopType.FOR);

                generator.emitComparingCommand();
                String loop = generator.getLoopBranch();
                generator.increaseLoopCount();
                generator.increaseBranch();

                generateChild(root, 3);
                generateChild(root, 2);

                generator.emitString(Instruction.GOTO + " " + loop);

                generator.emitString("\n" + branch + ":\n");

                break;

            case SyntaxProductionInit.While_LP_Test_Rp_TO_Statement:
                generator.emitLoopBranch();
                String branch2 = generator.getCurrentBranch();

                generateChild(root, 0);
                generator.emitComparingCommand();

                String loop2 = generator.getLoopBranch();
                generator.increaseLoopCount();
                generator.increaseBranch();

                generateChild(root, 1);

                generator.emitString(Instruction.GOTO + " " + loop2);

                generator.emitString("\n" + branch2 + ":\n");

                break;

            case SyntaxProductionInit.Do_Statement_While_Test_To_Statement:
                do {
                    generateChild(root, 0);
                } while (isLoopContinue(root, LoopType.DO_WHILE));

                break;

            case SyntaxProductionInit.Return_Semi_TO_Statement:
                isContinueExecution(false);

                break;

            case SyntaxProductionInit.Return_Expr_Semi_TO_Statement:
                node = generateChild(root, 0);
                Object obj = node.getAttribute(NodeKey.VALUE);
                setReturnObj(obj);
                isContinueExecution(false);
                break;

            default:
                generateChildren(root);

                break;
        }

        return root;
    }

    private boolean isLoopContinue(AstNode root, LoopType type) {
        AstNode res = null;
        if (type == LoopType.FOR || type == LoopType.DO_WHILE) {
            res = generateChild(root, 1);
        } else if (type == LoopType.WHILE) {
            res = generateChild(root, 0);
        }

        int result = (Integer) res.getAttribute(NodeKey.VALUE);

        return res != null && result != 0;
    }
}
