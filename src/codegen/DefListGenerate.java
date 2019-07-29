package codegen;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;

public class DefListGenerate extends BaseGenerate {

	@Override
	public Object generate(AstNode root) {
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
