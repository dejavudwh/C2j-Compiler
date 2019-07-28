package codegen;

import ast.AstNode;

public class StatementListExecutor extends BaseExecutor{
	 @Override 
	 public Object Execute(AstNode root) {
	    	executeChildren(root);
	    	Object child = root.getChildren().get(0);
	        copyChild(root, root.getChildren().get(0));
	    	return root;
	    }
}
