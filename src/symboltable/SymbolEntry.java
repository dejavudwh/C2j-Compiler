package symboltable;

/**
 * @author dejavudwh isHudw
 */

public class SymbolEntry {
    private Symbol symbol;
    private SymbolEntry prev, next;

    public SymbolEntry(Symbol sym) {
        this.symbol = sym;
    }

    public void setPrev(SymbolEntry prev) {
        this.prev = prev;
    }

    public void setNext(SymbolEntry next) {
        this.next = next;
    }

    public SymbolEntry getPrev() {
        return prev;
    }

    public SymbolEntry getNext() {
        return next;
    }
}
