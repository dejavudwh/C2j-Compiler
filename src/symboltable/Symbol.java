package symboltable;

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

    Symbol args = null;
    Object value = null;

    Symbol next = null;

    TypeLink  typeLinkBegin = null;
    TypeLink  typeLinkEnd = null;

    public Symbol(String name, int level) {
        this.name = name;
        this.level = level;
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

}

