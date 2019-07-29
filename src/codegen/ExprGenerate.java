package codegen;
import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

/**
 *
 * @author dejavudwh isHudw
 */

public class ExprGenerate extends BaseGenerate {
    @Override 
    public Object generate(AstNode root) {
    	generateChildren(root);
    	int production = (int)root.getAttribute(NodeKey.PRODUCTION);
    	
    	switch (production) {
			case SyntaxProductionInit.NoCommaExpr_TO_Expr:
				copyChild(root, root.getChildren().get(0));
				break;

			default:
				break;
    	}
    	
    	return root;
    }
}
