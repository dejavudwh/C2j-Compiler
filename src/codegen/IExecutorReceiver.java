package codegen;


import ast.AstNode;

public interface IExecutorReceiver {
    public void handleExecutorMessage(AstNode code);
}
