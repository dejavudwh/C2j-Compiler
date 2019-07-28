package codegen;

import ast.AstNode;

public class TestExecutor extends BaseExecutor {

	@Override
	public Object Execute(AstNode root) {
		executeChildren(root);
		copyChild(root, root.getChildren().get(0));
		
		return root;
	}

}
