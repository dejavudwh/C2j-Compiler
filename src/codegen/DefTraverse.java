package codegen;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;
import symboltable.Declarator;
import symboltable.Specifier;
import symboltable.Symbol;

/**
 * @author dejavudhw isHudw
 */

public class DefTraverse extends BaseTraverse {
    @Override
    public Object traverse(AstNode root) {
        int production = (int) root.getAttribute(NodeKey.PRODUCTION);
        Generator generator = Generator.getInstance();
        Symbol symbol = (Symbol) root.getAttribute(NodeKey.SYMBOL);
        switch (production) {
            case SyntaxProductionInit.Specifiers_DeclList_Semi_TO_Def:
                Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
                if (declarator != null) {
                    if (symbol.getSpecifierByType(Specifier.STRUCTURE) == null) {
                        /* If it's an array of structs, leave it to something else */
                        generator.createArray(symbol);
                    }
                } else {
                    /* Initialization variable */
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
