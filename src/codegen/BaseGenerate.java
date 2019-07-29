 package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.backend.ProgramGenerator;

public abstract class BaseGenerate implements Generate {
	private static boolean continueExecute = true;
	private static Object  returnObj = null;
	GenerateBrocaster executorBrocaster = null;
	ProgramGenerator generator;
	public static boolean inIfElseStatement = false;
	public static boolean isCompileMode = false;
	//change here 
	public static boolean resultOnStack = false;
	//change here 
	public static String funcName = "";
	
	
	
	public BaseGenerate() {
		executorBrocaster = GenerateBrocasterImpl.getInstance();
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
    	GenerateFactory factory = GenerateFactory.getGenerateFactory();
    	root.reverseChildren();
    	
    	int i = 0;
    	while (i < root.getChildren().size()) {
    		
    		if (continueExecute != true) {
    			break;
    		}
    		
    		AstNode child = root.getChildren().get(i);
    		
    		
    		executorBrocaster.brocastBeforeExecution(child);
    		
    		Generate generate = factory.getExecutor(child);
    		if (generate != null) {
    			generate.generate(child);
    		}
    		else {
    			System.err.println("Not suitable Generate found, node is: " + child.toString());
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
    	GenerateFactory factory = GenerateFactory.getGenerateFactory();
		child = (AstNode)root.getChildren().get(childIdx);
		Generate generate = factory.getExecutor(child);
    	AstNode res = (AstNode) generate.generate(child);
    	
    	return res;
    }
}
