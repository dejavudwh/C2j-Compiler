package codegen;

import ast.AstNode;

public class InitializerExecutor extends BaseExecutor {

	@Override
	public Object Execute(AstNode root) {
		//第0个孩子节点是Expr节点
		executeChild(root, 0);
		copyChild(root, root.getChildren().get(0));
		
		return root;
	}

}
