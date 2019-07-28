package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.generator.Instruction;
import codegen.generator.ProgramGenerator;

public class IfStatementExecutor extends BaseExecutor {

	 @Override 
	 public Object Execute(AstNode root) {
		 ProgramGenerator generator = ProgramGenerator.getInstance();
    	 AstNode res = executeChild(root, 0);
    	 
    	 String curBranch = generator.getCurrentBranch();
    	 generator.emitComparingCommand();
    	 
    	 
    	 Integer val = (Integer)res.getAttribute(NodeKey.VALUE);
    	 copyChild(root, res);  
    	 if ((val != null && val != 0) || BaseExecutor.isCompileMode) {
    		 generator.incraseIfElseEmbed();
    		 boolean b = BaseExecutor.inIfElseStatement;
    		 BaseExecutor.inIfElseStatement = false;
    		 executeChild(root, 1);
    		 BaseExecutor.inIfElseStatement = b;
    		 generator.decraseIfElseEmbed();
    	 }
    	 
    	 if (BaseExecutor.inIfElseStatement == true) {
    		 String branchOut = generator.getBranchOut();
    		 generator.emitString(Instruction.GOTO.toString() + " " + branchOut);	 
    	 } else {
    		 generator.emitString(curBranch + ":\n");
    		 generator.increaseBranch();
    	 }
    	
    	 
	    	
	    return root;
	}

}
