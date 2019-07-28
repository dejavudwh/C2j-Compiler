 package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.generator.ProgramGenerator;

public abstract class BaseExecutor implements Executor{
	private static boolean continueExecute = true;
	private static Object  returnObj = null;
	IExecutorBrocaster  executorBrocaster = null;
	ProgramGenerator generator;
	public static boolean inIfElseStatement = false;
	public static boolean isCompileMode = false;
	//change here 
	public static boolean resultOnStack = false;
	//change here 
	public static String funcName = "";
	
	
	
	public BaseExecutor() {
		executorBrocaster = ExecutorBrocasterImpl.getInstance();
		generator = ProgramGenerator.getInstance();
	}
	
	protected void setReturnObj(Object obj) {
	    this.returnObj = obj;	
	}
	
	protected Object getReturnObj() {
		return returnObj;
	}
	
	protected void clearReturnObj() {
		this.returnObj = null;
	}
	
	protected void isContinueExecution(boolean execute) {
		this.continueExecute = execute;
	}
	
    protected void executeChildren(AstNode root) {
    	ExecutorFactory factory = ExecutorFactory.getExecutorFactory();
    	root.reverseChildren();
    	
    	int i = 0;
    	while (i < root.getChildren().size()) {
    		
    		if (continueExecute != true) {
    			break;
    		}
    		
    		AstNode child = root.getChildren().get(i);
    		
    		
    		executorBrocaster.brocastBeforeExecution(child);
    		
    		Executor executor = factory.getExecutor(child);
    		if (executor != null) {
    			executor.Execute(child);	
    		}
    		else {
    			System.err.println("Not suitable Executor found, node is: " + child.toString());
    		}
    		
    		executorBrocaster.brocastAfterExecution(child);
    		
    		i++;
    	}
    }
    
    
    protected void copyChild(AstNode root, AstNode child) {
    	root.setAttribute(NodeKey.SYMBOL, child.getAttribute(NodeKey.SYMBOL));
    	root.setAttribute(NodeKey.VALUE, child.getAttribute(NodeKey.VALUE));
    	root.setAttribute(NodeKey.TEXT, child.getAttribute(NodeKey.TEXT));
    }
    
    protected AstNode executeChild(AstNode root, int childIdx) {
    	//把孩子链表的倒转放入到节点本身，减少逻辑耦合性
    	root.reverseChildren();
    	AstNode child;
    	ExecutorFactory factory = ExecutorFactory.getExecutorFactory();
		child = (AstNode)root.getChildren().get(childIdx);
		Executor executor = factory.getExecutor(child);
    	AstNode res = (AstNode)executor.Execute(child);
    	
    	return res;
    }
}
