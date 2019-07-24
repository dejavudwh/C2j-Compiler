package symboltable;

/**
 * Symbolic representation
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

    public Symbol(String name, int level) {
        this.name = name;
        this.level = level;
    }
}

