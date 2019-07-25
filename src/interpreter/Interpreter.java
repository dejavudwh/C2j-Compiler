package interpreter;

import ast.AstNode;

public class Interpreter implements Executor {
    private static Interpreter instance = null;

    private Interpreter() {}

    public static Interpreter getInstance() {
        if (instance == null) {
            instance = new Interpreter();
        }

        return instance;
    }

    @Override
    public Object execute(AstNode root) {
        if (root == null) {
            return null;
        }

        ExecutorFactory factory = ExecutorFactory.getInstance();
        Executor executor = factory.getExecutor(root);
        executor.execute(root);

        return root;
    }
}
