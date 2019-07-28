package codegen;

import symboltable.Symbol;

public interface IValueSetter {
   public void setValue(Object obj) throws Exception;
   public Symbol getSymbol();
}
