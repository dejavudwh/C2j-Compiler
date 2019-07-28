package codegen;

import ast.AstNode;

public class CodeGen implements Executor{

	private static CodeGen codegen = null;
	public static CodeGen getInstance() {
		if (codegen == null) {
			codegen = new CodeGen();
		}
		
		return codegen;
	}
	
	private CodeGen() {
		
	}
	
	@Override
	public Object Execute(AstNode root) {
		if (root == null) {
			return null;
		}
		
		ExecutorFactory factory = ExecutorFactory.getExecutorFactory();
		Executor executor = factory.getExecutor(root);
		executor.Execute(root);
		
		return root;
	}
}
