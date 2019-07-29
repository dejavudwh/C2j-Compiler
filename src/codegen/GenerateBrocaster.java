package codegen;

import ast.AstNode;

public interface GenerateBrocaster {
    public void brocastBeforeExecution(AstNode node);
    public void brocastAfterExecution(AstNode node);
    public void registerReceiverForBeforeExe(GenerateReceiver receiver);
    public void registerReceiverForAfterExe(GenerateReceiver receiver);
    public void removeReceiver(GenerateReceiver receiver);
}
