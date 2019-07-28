package codegen;

import ast.AstNode;

public class CompoundStmtExecutor extends BaseExecutor {

	@Override
	public Object Execute(AstNode root) {
		
		return executeChild(root, 0);
	}

}
