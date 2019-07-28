package codegen;

import ast.AstNode;

public interface Executor {
    public Object Execute(AstNode root);
}
