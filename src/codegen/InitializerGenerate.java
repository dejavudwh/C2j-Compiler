package codegen;

import ast.AstNode;

public class InitializerGenerate extends BaseGenerate {

	@Override
	public Object generate(AstNode root) {
		//第0个孩子节点是Expr节点
		executeChild(root, 0);
		copyChild(root, root.getChildren().get(0));
		
		return root;
	}

}
