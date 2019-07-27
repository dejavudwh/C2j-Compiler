package codegen;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

/**
 *
 * @author dejavudwh isHudw
 */

public class DefListTraverse extends BaseTraverse {

    @Override
    public Object traverse(AstNode root) {
        int production = (int)root.getAttribute(NodeKey.PRODUCTION);

        switch (production) {
            case SyntaxProductionInit.Def_To_DefList:
                traverseChild(root,0);
                break;

            case SyntaxProductionInit.DefList_Def_TO_DefList:
                traverseChild(root, 0);
                traverseChild(root, 1);
                break;

            default:
                break;
        }

        return root;
    }
}
