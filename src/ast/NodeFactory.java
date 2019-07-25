package ast;

import lexer.Token;

/**
 *
 * @author dejavudwh isHudw
 */

public class NodeFactory {
    public static AstNode createICodeNode(Token type) {
        return new AstNodeImpl(type);
    }
}
