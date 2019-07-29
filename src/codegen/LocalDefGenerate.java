package codegen;


import ast.AstNode;

public class LocalDefGenerate extends BaseGenerate {

	@Override
	public Object generate(AstNode root) {
		generateChild(root, 0);
		
		return root;
	}

}
