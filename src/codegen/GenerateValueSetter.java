package codegen;

import symboltable.Symbol;

public interface GenerateValueSetter {
   public void setValue(Object obj) throws Exception;
   public Symbol getSymbol();
}
