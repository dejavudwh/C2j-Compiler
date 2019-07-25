package interpreter;

import ast.AstNode;

/**
 * LocalDefs_StmtList_TO_CompoundStmt
 * @author dejavudwh isHudw
 */

public class CompoundStmtExecutor extends BaseExecutor {
    @Override
    public Object execute(AstNode root) {
        return executeChild(root, 0);
    }
}
