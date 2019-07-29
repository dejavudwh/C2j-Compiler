package codegen;

import ast.AstNode;

public class InitializerGenerate extends BaseGenerate {

	@Override
	public Object generate(AstNode root) {
		generateChild(root, 0);
		copyChild(root, root.getChildren().get(0));
		
		return root;
	}

}
