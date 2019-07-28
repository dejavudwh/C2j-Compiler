package codegen.generator;

public enum Directive {
    CLASS_PUBLIC(".class public"),
    END_CLASS(".end class"),
    SUPER(".super"),
    FIELD_PRIVATE_STATIC(".field private static"),
    METHOD_STATIC(".method static"),
    METHOD_PUBLIC(".method public"),
    FIELD_PUBLIC(".field public"),
    METHOD_PUBBLIC_STATIC(".method public static"),
    END_METHOD(".end method"),
    LIMIT_LOCALS(".limit locals"),
    LIMIT_STACK(".limit stack"),
    VAR(".var"),
    LINE(".line");
    
    private String text;
    
    Directive(String text) {
    	this.text = text;
    }
    
    public String toString() {
    	return text;
    }
}
