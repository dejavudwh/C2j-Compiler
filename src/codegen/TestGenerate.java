package codegen;

import ast.AstNode;

public class TestGenerate extends BaseGenerate {

	@Override
	public Object generate(AstNode root) {
		generateChildren(root);
		copyChild(root, root.getChildren().get(0));
		
		return root;
	}

}
