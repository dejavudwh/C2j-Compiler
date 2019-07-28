package codegen;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

public class DefListExecutor extends BaseExecutor {

	@Override
	public Object Execute(AstNode root) {
		int production = (int)root.getAttribute(NodeKey.PRODUCTION);
		
		switch (production) {
		case SyntaxProductionInit.Def_To_DefList:
			executeChild(root,0);
			break;
		case SyntaxProductionInit.DefList_Def_TO_DefList:
			executeChild(root, 0);
			executeChild(root, 1);
			
			break;
		}
		
		return root;
	}

}
