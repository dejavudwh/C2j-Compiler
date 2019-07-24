package symboltable;

public class TypeLink {
    public boolean isDeclarator;
    /**
     * typedef int
     */
    public boolean isTypeDef;
    /**
     * Specifier or Declarator
     */
    public Object typeObject;

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
