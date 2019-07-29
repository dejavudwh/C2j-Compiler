package codegen;


import ast.AstNode;

public class LocalDefGenerate extends BaseGenerate {

	@Override
	public Object generate(AstNode root) {
		executeChild(root, 0);
		
		return root;
	}

}
