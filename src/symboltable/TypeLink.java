package symboltable;

public class TypeLink {
    boolean isDeclarator;
    /**
     * typedef int
     */
    boolean isTypeDef;
    /**
     * Specifier or Declarator
     */
    Object typeObject;

    private TypeLink next = null;

    public TypeLink(boolean isDeclarator, boolean typeDef, Object typeObj) {
        this.isDeclarator = isDeclarator;
        this.isTypeDef = typeDef;
        this.typeObject = typeObj;
    }

    public Object getTypeObject() {
        return typeObject;
    }

    public TypeLink toNext() {
        return next;
    }

    public void setNextLink(TypeLink obj) {
        this.next = obj;
    }

}
