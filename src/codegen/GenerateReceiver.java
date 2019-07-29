package codegen;


import ast.AstNode;

public interface GenerateReceiver {
    public void handleExecutorMessage(AstNode code);
}
