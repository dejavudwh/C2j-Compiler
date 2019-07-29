package codegen;

import ast.AstNode;

public class StatementListGenerate extends BaseGenerate {
	 @Override 
	 public Object generate(AstNode root) {
	    	executeChildren(root);
	    	Object child = root.getChildren().get(0);
	        copyChild(root, root.getChildren().get(0));
	    	return root;
	    }
}
