package symboltable;

import parse.LRStateTableParser;

/**
 * Symbolic representation
 *
 * @author dejavudwh isHudw
 */

public class Symbol {
    String name;
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

    public void setArgList(Symbol symbol) {
        this.args = symbol;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        Symbol symbol = (Symbol) obj;
        return (this.name.equals(symbol.name) && this.level == symbol.level &&
                this.symbolScope.equals(symbol.symbolScope));
    }

}

