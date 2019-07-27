package codegen;

import ast.AstNode;

public interface Traverse {
    /** Traverse the AST generated code */
    Object traverse(AstNode root);
}
