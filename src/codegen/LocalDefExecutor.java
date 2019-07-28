package codegen;


import ast.AstNode;

public class LocalDefExecutor extends BaseExecutor {

	@Override
	public Object Execute(AstNode root) {
		executeChild(root, 0);
		
		return root;
	}

}
