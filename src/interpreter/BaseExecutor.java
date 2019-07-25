package interpreter;

import ast.AstNode;

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
            }
            else {
                System.err.println("Not suitable Executor found, node is: " + child.toString());
            }

            executorBrocaster.brocastAfterExecution(child);

            i++;
        }
    }
}
