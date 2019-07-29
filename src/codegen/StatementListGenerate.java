package codegen;

import ast.AstNode;

public class StatementListGenerate extends BaseGenerate {
	 @Override 
	 public Object generate(AstNode root) {
	    	generateChildren(root);
	        copyChild(root, root.getChildren().get(0));
	    	return root;
	    }
}
