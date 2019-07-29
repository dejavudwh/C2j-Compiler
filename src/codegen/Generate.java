package codegen;

import ast.AstNode;

public interface Generate {
    public Object generate(AstNode root);
}
