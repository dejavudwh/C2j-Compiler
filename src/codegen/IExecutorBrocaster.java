package codegen;

import ast.AstNode;

public interface IExecutorBrocaster {
    public void brocastBeforeExecution(AstNode node);
    public void brocastAfterExecution(AstNode node);
    public void registerReceiverForBeforeExe(IExecutorReceiver receiver);
    public void registerReceiverForAfterExe(IExecutorReceiver receiver);
    public void removeReceiver(IExecutorReceiver receiver);
}
