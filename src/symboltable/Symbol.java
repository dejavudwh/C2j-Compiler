package symboltable;

import debug.ConsoleDebugColor;
import interpreter.ValueSetter;
import parse.LRStateTableParser;

/**
 * Symbolic representation
 *
 * @author dejavudwh isHudw
 */

public class Symbol implements ValueSetter {
    public String name;
    String rname;

    int level;
    boolean implicit;
    boolean duplicate;

    public Symbol args = null;
    public Object value = null;

    Symbol next = null;

    public TypeLink typeLinkBegin = null;
    public TypeLink typeLinkEnd = null;

    private String symbolScope = LRStateTableParser.GLOBAL_SCOPE;

    public Symbol(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public Symbol copy() {
        Symbol symbol = new Symbol(this.name, this.level);
        symbol.args = this.args;
        symbol.next = this.next;
        symbol.value = this.value;
        symbol.typeLinkBegin = this.typeLinkBegin;
        symbol.typeLinkEnd = this.typeLinkEnd;
        symbol.symbolScope = this.symbolScope;

        return symbol;
    }

    public void addDeclarator(TypeLink type) {
        if (typeLinkBegin == null) {
            typeLinkBegin = type;
            typeLinkEnd = type;
        } else {
            type.setNextLink(typeLinkBegin);
            typeLinkBegin = type;
        }
    }

    public void addSpecifier(TypeLink type) {
        if (typeLinkBegin == null) {
            typeLinkBegin = type;
            typeLinkEnd = type;
        } else {
            typeLinkEnd.setNextLink(type);
            typeLinkEnd = type;
        }
    }

    public Symbol getNextSymbol() {
        return next;
    }

    public String getScope() {
        return symbolScope;
    }

    public int getLevel() {
        return level;
    }

    public void setNextSymbol(Symbol symbol) {
        this.next = symbol;
    }

    public void addScope(String scope) {
        this.symbolScope = scope;
    }

    public Symbol getArgList() {
        return args;
    }

    public void setArgList(Symbol symbol) {
        this.args = symbol;
    }

    public String getName() {
        return name;
    }

    public Declarator getDeclarator(int type) {
        TypeLink begin = typeLinkBegin;
        while (begin != null && begin.getTypeObject() != null) {
            if (begin.isDeclarator) {
                Declarator declarator = (Declarator) begin.getTypeObject();
                if (declarator.getType() == type) {
                    return declarator;
                }
            }

            begin = begin.toNext();
        }

        return null;
    }

    public TypeLink getTypeHead() {
        return typeLinkBegin;
    }

    public Object getValue() {
        return value;
    }

    public int getByteSize() {
        int size = 0;
        TypeLink head = typeLinkBegin;
        while (head != null) {
            if (!head.isDeclarator) {
                Specifier sp = (Specifier) head.typeObject;
                if (sp.isLong() || sp.getType() == Specifier.INT ||
                        getDeclarator(Declarator.POINTER) != null) {
                    size = 4;
                    break;
                } else {
                    size = 1;
                    break;
                }
            }

            head = head.toNext();
        }

        return size;
    }

    @Override
    public void setValue(Object obj) {
        if (obj != null) {
            ConsoleDebugColor.outlnPurple("Assign Value of " + obj.toString() + " to Variable " + name);
        }

        this.value = obj;
    }

    @Override
    public Symbol getSymbol() {
        return this;
    }

    /** The following method is required only in code generation */
    public Specifier getSpecifierByType(int type) {
        TypeLink head = typeLinkBegin;
        while (head != null) {
            if (!head.isDeclarator) {
                Specifier sp = (Specifier) head.typeObject;
                if (sp.getType() == type) {
                    return sp;
                }
            }

            head = head.toNext();
        }

        return null;
    }

    public boolean hasType(int type) {
        TypeLink  head = typeLinkBegin;
        while (head != null) {
            if (!head.isDeclarator) {
                Specifier sp = (Specifier)head.typeObject;
                if (sp.getType() == type) {
                    return true;
                }
            }

            head = head.toNext();
        }


        return false;
    }

    @Override
    public boolean equals(Object obj) {
        Symbol symbol = (Symbol) obj;
        return (this.name.equals(symbol.name) && this.level == symbol.level &&
                this.symbolScope.equals(symbol.symbolScope));
    }

}

