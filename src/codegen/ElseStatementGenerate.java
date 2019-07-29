package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.backend.ProgramGenerator;

public class ElseStatementGenerate extends BaseGenerate {
    private ProgramGenerator generator = ProgramGenerator.getInstance();
	@Override
	public Object generate(AstNode root) {
		 BaseGenerate.inIfElseStatement = true;
		 
		//先执行if 部分
		 String branch = generator.getCurrentBranch();
    	 AstNode res = executeChild(root, 0);
    	 
    	 BaseGenerate.inIfElseStatement = false;
    	 
		 branch = "\n" + branch + ":\n";
		 generator.emitString(branch);
		 
		/*
		 if (backend.getIfElseEmbedCount() == 0) {
			 backend.increaseBranch();
		 }*/
		 //change here
		 generator.increaseBranch();
		 
    	 Object obj = res.getAttribute(NodeKey.VALUE);
    	 if ((Integer)obj == 0 || BaseGenerate.isCompileMode) {
    		 generator.incraseIfElseEmbed();
    		 //if 部分没有执行，所以执行else部分
    		 res = executeChild(root, 1); 
    		 generator.decraseIfElseEmbed();
    	 }
    	 
    	 copyChild(root, res);
    	 
    	 generator.emitBranchOut();
		 
    	 return root;
	}

}
