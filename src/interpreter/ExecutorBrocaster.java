package interpreter;

import ast.AstNode;

/**
 *
 * @author dejavudhw isHudw
 */

public interface ExecutorBrocaster {
    void brocastBeforeExecution(AstNode node);
    void brocastAfterExecution(AstNode node);
    void registerReceiverForBeforeExe(ExecutorReceiver receiver);
    void registerReceiverForAfterExe(ExecutorReceiver receiver);
    void removeReceiver(ExecutorReceiver receiver);
}
