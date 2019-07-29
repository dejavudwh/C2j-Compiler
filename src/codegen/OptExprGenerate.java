package codegen;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

public class OptExprGenerate extends BaseGenerate {

	@Override
	public Object generate(AstNode root) {
		int production = (int)root.getAttribute(NodeKey.PRODUCTION);
		switch (production) {
		case SyntaxProductionInit.Semi_TO_OptExpr:
			return root;
			
		case SyntaxProductionInit.Expr_Semi_TO_OptExpr:
			return generateChild(root, 0);
			
		default:
				return root;
		}
	}

}
