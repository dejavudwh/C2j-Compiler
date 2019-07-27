package codegen;

import ast.AstNode;

public interface GeneratorBrocaster {
    void brocastBeforeExecution(AstNode node);
    void brocastAfterExecution(AstNode node);
    void registerReceiverForBeforeExe(GeneratorReceiver receiver);
    void registerReceiverForAfterExe(GeneratorReceiver receiver);
    void removeReceiver(GeneratorReceiver receiver);
}
