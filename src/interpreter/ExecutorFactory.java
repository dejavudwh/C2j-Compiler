package interpreter;

import ast.AstNode;

/**
 *
 * @author dejavdwh isHudw
 */

public class ExecutorFactory {
    private static ExecutorFactory instance = null;
    private ExecutorFactory() {

    }

    public static ExecutorFactory getInstance() {
        if (instance == null) {
            instance = new ExecutorFactory();
        }

        return instance;
    }

    public Executor getExecutor(AstNode node) {
        return null;
    }
}
