package codegen;

import ast.AstNode;

public class CompoundStmtGenerate extends BaseGenerate {

	@Override
	public Object generate(AstNode root) {
		
		return executeChild(root, 0);
	}

}
