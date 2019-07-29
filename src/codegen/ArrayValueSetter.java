package codegen;

import codegen.backend.ProgramGenerator;
import debug.ConsoleDebugColor;
import interpreter.ValueSetter;
import symboltable.*;

public class ArrayValueSetter implements ValueSetter {
	private Symbol symbol;
	private int index = 0;
	private Object indexObj = null;
	
    @Override
    public void setValue(Object obj) {
    	Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
    	try {
			declarator.addElement(index, obj);
			if (indexObj == null) {
			    ProgramGenerator.getInstance().writeArrayElement(symbol, index, obj);
			} else {
				ProgramGenerator.getInstance().writeArrayElement(symbol, indexObj, obj);
			}
			ConsoleDebugColor.outlnPurple("Set Value of " + obj.toString() + " to Array of name " + symbol.getName() + " with index of " + index);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
    	
    }
    
    public ArrayValueSetter(Symbol symbol, Object index) {
    	this.symbol = symbol;
    	if (index instanceof Integer) {
    	    this.index = (int)index;
    	} else {
    		this.indexObj = index;
    	}
    }

	@Override
	public Symbol getSymbol() {
		// TODO Auto-generated method stub
		return symbol;
	}
	
	//change here
	public Object getIndex() {
		if (indexObj != null) {
			return indexObj;
		}
		
		return index;
	}
}
