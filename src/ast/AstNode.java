package ast;

import java.util.ArrayList;

/**
 * @author dejavudwh isHudw
 */

public interface AstNode {

    AstNode addChild(AstNode node);

    AstNode getParent();

    ArrayList<AstNode> getChildren();

    void setAttribute(NodeKey key, Object value);

    Object getAttribute(NodeKey key);

    boolean isChildrenReverse();

    void reverseChildren();

    AstNode copy();

}
