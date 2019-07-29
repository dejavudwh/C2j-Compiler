package codegen.backend;

public enum Instruction {
    LDC("ldc"),
    
    GETSTATIC("getstatic"),
    SIPUSH("sipush"),
    IADD("iadd"),
    IMUL("imul"),
    ISUB("isub"),
    IDIV("idiv"),
    INVOKEVIRTUAL("invokevirtual"),
    INVOKESTATIC("invokestatic"),
    INVOKESPECIAL("invokespecial"),
    RETURN("return"),
    IRETURN("ireturn"),
    ILOAD("iload"),
    ISTORE("istore"),
    NEWARRAY("newarray"),
    NEW("new"),
    DUP("dup"),
    ASTORE("astore"),
    IASTORE("iastore"),
    ALOAD("aload"),
    PUTFIELD("putfield"),
    GETFIELD("getfield"),
    ANEWARRAY("anewarray"),
    AASTORE("aastore"),
    AALOAD("aaload"),
    IF_ICMPEG("if_icmpeq"),  
    IF_ICMPNE("if_icmpne"),
    IF_ICMPLT("if_icmplt"),
    IF_ICMPGE("if_icmpge"),
    IF_ICMPGT("if_icmpgt"),
    IF_ICMPLE("if_icmple"),
    GOTO("goto"),
    IALOAD("iaload");
    
    private String text;
    Instruction(String s) {
    	this.text = s;
    }
    
    public String toString() {
    	return text;
    }
}
