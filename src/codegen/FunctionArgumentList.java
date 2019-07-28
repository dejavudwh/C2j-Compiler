package codegen;

import java.util.ArrayList;
import java.util.Collections;

public class FunctionArgumentList {
    private static FunctionArgumentList argumentList = null;
    private ArrayList<Object> funcArgList = new ArrayList<Object>();
    private ArrayList<Object> argSymList = new ArrayList<Object>();
    
    public static FunctionArgumentList getFunctionArgumentList() {
    	if (argumentList == null) {
    		argumentList = new FunctionArgumentList();
    	}
    	
    	return argumentList;
    }
    
    public void setFuncArgList(ArrayList<Object> list) {
    	funcArgList = list;
    }
    
    public void setFuncArgSymbolList(ArrayList<Object> list) {
    	this.argSymList = list;
    }
    
    public ArrayList<Object> getFuncArgList(boolean reverse) {
    	
    	if (reverse == true) {
    		Collections.reverse(funcArgList);
    	}
    	
    	return funcArgList;
    }
    
    public ArrayList<Object> getFuncArgSymsList(boolean reverse) {
    	if (reverse == true) {
    		Collections.reverse(argSymList);
    	}
    	
    	return argSymList;
    }
    
	private FunctionArgumentList() {}
}
