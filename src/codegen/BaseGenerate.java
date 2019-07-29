package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.backend.ProgramGenerator;

public abstract class BaseGenerate implements Generate {
    private static boolean continueExecute = true;
    private static Object returnObj = null;
    private GenerateBrocaster generateBrocaster;
    ProgramGenerator generator;
    public static boolean inIfElseStatement = false;
    public static boolean resultOnStack = false;
    public static String funcName = "";

    public BaseGenerate() {
        generateBrocaster = GenerateBrocasterImpl.getInstance();
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

    protected void generateChildren(AstNode root) {
        GenerateFactory factory = GenerateFactory.getGenerateFactory();
        root.reverseChildren();

        int i = 0;
        while (i < root.getChildren().size()) {

            if (!continueExecute) {
                break;
            }

            AstNode child = root.getChildren().get(i);

            generateBrocaster.brocastBeforeExecution(child);

            Generate generate = factory.getGenerate(child);
            if (generate != null) {
                generate.generate(child);
            } else {
                System.err.println("Not suitable Generate found, node is: " + child.toString());
            }

            generateBrocaster.brocastAfterExecution(child);

            i++;
        }
    }

    protected void copyChild(AstNode root, AstNode child) {
        root.setAttribute(NodeKey.SYMBOL, child.getAttribute(NodeKey.SYMBOL));
        root.setAttribute(NodeKey.VALUE, child.getAttribute(NodeKey.VALUE));
        root.setAttribute(NodeKey.TEXT, child.getAttribute(NodeKey.TEXT));
    }

    protected AstNode generateChild(AstNode root, int childIdx) {
        root.reverseChildren();
        AstNode child;
        GenerateFactory factory = GenerateFactory.getGenerateFactory();
        child = (AstNode) root.getChildren().get(childIdx);
        Generate generate = factory.getGenerate(child);
        AstNode res = (AstNode) generate.generate(child);

        return res;
    }
}
