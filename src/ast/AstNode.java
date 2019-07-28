package ast;

import java.util.ArrayList;

/**
 * @author dejavudwh isHudw
 */

public interface AstNode {

    public AstNode addChild(AstNode node);

    public AstNode getParent();

    public ArrayList<AstNode> getChildren();

    public void setAttribute(NodeKey key, Object value);

    public Object getAttribute(NodeKey key);

    public boolean isChildrenReverse();

    public void reverseChildren();

    public AstNode copy();

}
