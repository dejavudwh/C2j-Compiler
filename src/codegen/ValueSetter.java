package codegen;

import symboltable.Symbol;

/**
 *
 * @author dejavudwh isHudw
 */

public interface ValueSetter {
    /** An interface that preserves assignment information for the code generation process */
    void setValue(Object obj) throws Exception;
    Symbol getSymbol();
}
