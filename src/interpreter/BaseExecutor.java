package interpreter;

import ast.AstNode;
import ast.NodeKey;

/**
 * @author dejavudwh isHudw
 */

public abstract class BaseExecutor implements Executor {
    ExecutorBrocaster executorBrocaster;
    private static boolean continueExecute = true;
    private static Object  returnObj = null;

    public BaseExecutor() {
        executorBrocaster = ExecutorBrocasterImpl.getInstance();
    }

    protected void executeChildren(AstNode root) {
        ExecutorFactory factory = ExecutorFactory.getInstance();
        root.reverseChildren();

        int i = 0;
        while (i < root.getChildren().size()) {

            if (!continueExecute) {
                break;
            }

            AstNode child = root.getChildren().get(i);

            executorBrocaster.brocastBeforeExecution(child);

            Executor executor = factory.getExecutor(child);
            if (executor != null) {
                executor.execute(child);
            } else {
                System.err.println("Not suitable Executor found, node is: " + child.toString());
            }

            executorBrocaster.brocastAfterExecution(child);

            i++;
        }
    }

    protected AstNode executeChild(AstNode root, int childIdx) {
        root.reverseChildren();
        AstNode child;
        ExecutorFactory factory = ExecutorFactory.getInstance();
        child = (AstNode)root.getChildren().get(childIdx);
        Executor executor = factory.getExecutor(child);
        AstNode res = (AstNode)executor.execute(child);

        return res;
    }

    protected void copyChild(AstNode root, AstNode child) {
        root.setAttribute(NodeKey.SYMBOL, child.getAttribute(NodeKey.SYMBOL));
        root.setAttribute(NodeKey.VALUE, child.getAttribute(NodeKey.VALUE));
        root.setAttribute(NodeKey.TEXT, child.getAttribute(NodeKey.TEXT));
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

}
