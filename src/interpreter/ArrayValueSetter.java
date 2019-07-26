package interpreter;

import debug.ConsoleDebugColor;
import symboltable.Declarator;
import symboltable.Symbol;

/**
 *
 * @author deajvudwh isHudw
 */

public class ArrayValueSetter implements ValueSetter {
    private Symbol symbol;
    private int index;

    public ArrayValueSetter(Symbol symbol, int index) {
        this.symbol = symbol;
        this.index = index;
    }

    @Override
    public void setValue(Object obj) {
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        try {
            declarator.addElement(index, obj);

            ConsoleDebugColor.outlnPurple("Set Value of " + obj.toString() + " to Array of name " + symbol.getName() + " with index of " + index);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public Symbol getSymbol() {
        return symbol;
    }
}
