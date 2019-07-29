package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.backend.Instruction;
import codegen.backend.ProgramGenerator;
import parse.SyntaxProductionInit;
import symboltable.*;

/**
 *
 * @author dejavudwh isHudw
 */

public class DefGenerate extends BaseGenerate {

    @Override
    public Object generate(AstNode root) {
        int production = (int) root.getAttribute(NodeKey.PRODUCTION);
        ProgramGenerator generator = ProgramGenerator.getInstance();
        Symbol symbol = (Symbol) root.getAttribute(NodeKey.SYMBOL);

        switch (production) {
            case SyntaxProductionInit.Specifiers_DeclList_Semi_TO_Def:
                Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
                if (declarator != null) {
                    if (symbol.getSpecifierByType(Specifier.STRUCTURE) == null) {
                        generator.createArray(symbol);
                    }
                } else {
                    int i = generator.getLocalVariableIndex(symbol);
                    generator.emit(Instruction.SIPUSH, "" + 0);
                    generator.emit(Instruction.ISTORE, "" + i);
                }

                break;

            default:
                break;
        }

        return root;
    }

}
