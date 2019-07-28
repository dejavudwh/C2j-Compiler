package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.generator.ProgramGenerator;

public class ElseStatementExecutor extends BaseExecutor {
    private ProgramGenerator generator = ProgramGenerator.getInstance();
	@Override
	public Object Execute(AstNode root) {
		 BaseExecutor.inIfElseStatement = true;
		 
		//先执行if 部分
		 String branch = generator.getCurrentBranch();
    	 AstNode res = executeChild(root, 0);
    	 
    	 BaseExecutor.inIfElseStatement = false;
    	 
		 branch = "\n" + branch + ":\n";
		 generator.emitString(branch);
		 
		/*
		 if (generator.getIfElseEmbedCount() == 0) {
			 generator.increaseBranch();
		 }*/
		 //change here
		 generator.increaseBranch();
		 
    	 Object obj = res.getAttribute(NodeKey.VALUE);
    	 if ((Integer)obj == 0 || BaseExecutor.isCompileMode) {
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
