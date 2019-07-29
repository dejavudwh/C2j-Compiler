package codegen;

import ast.AstNode;

public class EndOptGenerate extends BaseGenerate {

	@Override
	public Object generate(AstNode root) {
		
		return generateChild(root, 0);
	}

}
