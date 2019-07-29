package codegen;

import ast.AstNode;

/**
 *
 * @author dejavudwh isHudw
 */

public class CodeGen implements Generate {

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
	public Object generate(AstNode root) {
		if (root == null) {
			return null;
		}
		
		GenerateFactory factory = GenerateFactory.getGenerateFactory();
		Generate generate = factory.getExecutor(root);
		generate.generate(root);
		
		return root;
	}
}
