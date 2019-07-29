package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.backend.Instruction;
import codegen.backend.ProgramGenerator;
import debug.ConsoleDebugColor;
import parse.SyntaxProductionInit;
import symboltable.*;

/**
 *
 * @author dejavudwh isHudw
 */

public class BinaryGenerate extends BaseGenerate {
    @Override
    public Object generate(AstNode root) {
    	generateChildren(root);
    	ProgramGenerator generator = ProgramGenerator.getInstance();
    	AstNode child;
    	int production = (int)root.getAttribute(NodeKey.PRODUCTION);
    	int val1 = 0, val2 = 0;
    	switch (production) {
    	case SyntaxProductionInit.Uanry_TO_Binary:
    		child = root.getChildren().get(0);
    		copyChild(root, child);
    		break;
    		
    	case SyntaxProductionInit.Binary_Plus_Binary_TO_Binary:
    	case SyntaxProductionInit.Binary_DivOp_Binary_TO_Binary:
    	case SyntaxProductionInit.Binary_Minus_Binary_TO_Binary:
    	case SyntaxProductionInit.Binary_Start_Binary_TO_Binary:
    		BaseGenerate.resultOnStack = true;
    		if (root.getChildren().get(0).getAttribute(NodeKey.VALUE) != null) {
    		    val1 = (Integer)root.getChildren().get(0).getAttribute(NodeKey.VALUE);
    		}
   		    Symbol sym1 = (Symbol)root.getChildren().get(0).getAttribute(NodeKey.SYMBOL);
   		    if (sym1 != null) {
   			   int d = generator.getLocalVariableIndex(sym1);
   			   generator.emit(Instruction.ILOAD, "" + d);
   		    } else {
   			   generator.emit(Instruction.SIPUSH, "" + val1);
   		    }
   		    
   		    if (root.getChildren().get(1).getAttribute(NodeKey.VALUE) != null) {
   		        val2 = (Integer)root.getChildren().get(1).getAttribute(NodeKey.VALUE);
   		    }
   		    Symbol sym2 = (Symbol)root.getChildren().get(1).getAttribute(NodeKey.SYMBOL);
   		    if (sym2 != null) {
   			   int d = generator.getLocalVariableIndex(sym2);
   			   generator.emit(Instruction.ILOAD, "" + d);
   		    } else {
   			   generator.emit(Instruction.SIPUSH, "" + val2);
   		    }

    		if (production == SyntaxProductionInit.Binary_Plus_Binary_TO_Binary) {
    			String text = root.getChildren().get(0).getAttribute(NodeKey.TEXT) + " plus " + root.getChildren().get(1).getAttribute(NodeKey.TEXT);
    			root.setAttribute(NodeKey.VALUE, val1 + val2);
    			root.setAttribute(NodeKey.TEXT,  text);
        		ConsoleDebugColor.outlnPurple(text + " is " + (val1+val2) );
        		ProgramGenerator.getInstance().emit(Instruction.IADD);
    		} else if (production ==  SyntaxProductionInit.Binary_Minus_Binary_TO_Binary) {
    			String text = root.getChildren().get(0).getAttribute(NodeKey.TEXT) + " minus " + root.getChildren().get(1).getAttribute(NodeKey.TEXT);
    			root.setAttribute(NodeKey.VALUE, val1 - val2);
    			root.setAttribute(NodeKey.TEXT,  text);
        		ConsoleDebugColor.outlnPurple(text + " is " + (val1-val2) );
        		ProgramGenerator.getInstance().emit(Instruction.ISUB);
    		} else if (production ==  SyntaxProductionInit.Binary_Start_Binary_TO_Binary) {
    			String text = root.getChildren().get(0).getAttribute(NodeKey.TEXT) + " * " + root.getChildren().get(1).getAttribute(NodeKey.TEXT);
    			root.setAttribute(NodeKey.VALUE, val1 * val2);
    			root.setAttribute(NodeKey.TEXT,  text);
        		ConsoleDebugColor.outlnPurple(text + " is " + (val1 * val2) );
        		
        		ProgramGenerator.getInstance().emit(Instruction.IMUL);
    		} else {
    			root.setAttribute(NodeKey.VALUE, val1 / val2);
        		ConsoleDebugColor.outlnPurple( root.getChildren().get(0).getAttribute(NodeKey.TEXT) + " is divided by "
        				+ root.getChildren().get(1).getAttribute(NodeKey.TEXT) + " and result is " + (val1/val2) );
        		ProgramGenerator.getInstance().emit(Instruction.IDIV);
    		}
    		
    		root.setAttribute(NodeKey.TEXT, "onstack");
    		
    		break;
    		
    	case SyntaxProductionInit.Binary_RelOP_Binary_TO_Binray:
    		 Object valObj = root.getChildren().get(0).getAttribute(NodeKey.VALUE);
    		 Object symObj = root.getChildren().get(0).getAttribute(NodeKey.SYMBOL);
    		 
    		 if (symObj == null) {
    		     val1 = (Integer)valObj;
    		     generator.emit(Instruction.SIPUSH, "" + val1);
    		 } else {
    			 if (symObj instanceof ArrayValueSetter) {
    				 sym1 = (Symbol)((ArrayValueSetter)symObj).getSymbol();
    				 Object index = ((ArrayValueSetter)symObj).getIndex();
    				 generator.readArrayElement(sym1, index);
    			 } else {
    		         sym1 = (Symbol)symObj;
    		         int d = generator.getLocalVariableIndex(sym1);
			         generator.emit(Instruction.ILOAD, "" + d);
    			 }
    		 }
    		 String operator = (String)root.getChildren().get(1).getAttribute(NodeKey.TEXT);
    		 
    		 valObj = root.getChildren().get(2).getAttribute(NodeKey.VALUE);
    		 symObj = root.getChildren().get(2).getAttribute(NodeKey.SYMBOL);
    		 if (symObj == null) {
    		     val2 = (Integer)valObj;
    		     generator.emit(Instruction.SIPUSH, "" + val2);
    		 } else {
    			 if (symObj instanceof ArrayValueSetter) {
    				 sym2 = (Symbol)((ArrayValueSetter)symObj).getSymbol();
    				 Object index = ((ArrayValueSetter)symObj).getIndex();
    				 generator.readArrayElement(sym2, index);
    			 } else {
    		         sym2 = (Symbol)symObj;
    		         int d = generator.getLocalVariableIndex(sym2);
			         generator.emit(Instruction.ILOAD, "" + d);
    			 }

    		 }
    		 String branch = ProgramGenerator.getInstance().getCurrentBranch() + "\n";
    		 
    		 switch (operator) {
    		 case "==":
    			 root.setAttribute(NodeKey.VALUE, val1 == val2 ? 1 : 0);
    			 break;

    		 case "<":
    			 generator.setComparingCommand(Instruction.IF_ICMPGE.toString() + " " + branch);
    			 root.setAttribute(NodeKey.VALUE, val1 < val2? 1 : 0);
    			 break;

    		 case "<=":
    			 generator.setComparingCommand(Instruction.IF_ICMPGT.toString() + " " + branch);
    			 root.setAttribute(NodeKey.VALUE, val1 <= val2? 1 : 0);
    			 break;

    		 case ">":
    			 generator.setComparingCommand(Instruction.IF_ICMPLE.toString() + " " + branch);
    			 root.setAttribute(NodeKey.VALUE, val1 > val2? 1 : 0);
    			 break;

    		 case ">=":
    			 generator.setComparingCommand(Instruction.IF_ICMPLT.toString() + " " + branch);
    			 root.setAttribute(NodeKey.VALUE, val1 >= val2? 1 : 0);
    			 break;
    			 
    		 case "!=":
    			 root.setAttribute(NodeKey.VALUE, val1 != val2? 1 : 0);
    			 break;

    		 default:
    		 	break;
    		 }
    		
    		 break;

    	default:
    		break;
    	
    	}
    	
    	return root;
    }
}
