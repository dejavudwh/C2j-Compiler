package codegen;

import ast.AstNode;

/**
 *
 * @author dejavudwh isHudw
 */

public interface GeneratorReceiver {
    void handleExecutorMessage(AstNode code);
}
