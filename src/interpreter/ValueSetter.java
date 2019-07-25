package interpreter;

import symboltable.Symbol;

/**
 *
 * @author dejavudwh isHudw
 */

public interface ValueSetter {
    void setValue(Object obj) throws Exception;
    Symbol getSymbol();
}
