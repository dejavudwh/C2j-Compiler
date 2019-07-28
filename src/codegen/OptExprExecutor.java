package codegen;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

public class OptExprExecutor extends BaseExecutor {

	@Override
	public Object Execute(AstNode root) {
		int production = (int)root.getAttribute(NodeKey.PRODUCTION);
		switch (production) {
		case SyntaxProductionInit.Semi_TO_OptExpr:
			return root;
			
		case SyntaxProductionInit.Expr_Semi_TO_OptExpr:
			return executeChild(root, 0);
			
		default:
				return root;
		}
	}

}
