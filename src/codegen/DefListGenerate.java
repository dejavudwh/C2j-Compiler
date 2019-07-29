package codegen;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

/**
 *
 * @author dejavudwh isHudw
 */

public class DefListGenerate extends BaseGenerate {

    @Override
    public Object generate(AstNode root) {
        int production = (int) root.getAttribute(NodeKey.PRODUCTION);

        switch (production) {
            case SyntaxProductionInit.Def_To_DefList:
                generateChild(root, 0);
                break;

            case SyntaxProductionInit.DefList_Def_TO_DefList:
                generateChild(root, 0);
                generateChild(root, 1);
                break;

            default:
                break;
        }

        return root;
    }

}
