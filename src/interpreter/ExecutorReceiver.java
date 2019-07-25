package interpreter;

import ast.AstNode;

/**
 *
 * @author dejavudwh isHudw
 */

public interface ExecutorReceiver {
    void handleExecutorMessage(AstNode code);
}
