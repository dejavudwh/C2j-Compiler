package codegen;

import ast.AstNode;
import ast.NodeKey;
import codegen.backend.Instruction;
import codegen.backend.ProgramGenerator;
import parse.SyntaxProductionInit;
import symboltable.*;


public class DefGenerate extends BaseGenerate {

	@Override
	public Object generate(AstNode root) {
		int production = (int)root.getAttribute(NodeKey.PRODUCTION);
		ProgramGenerator generator = ProgramGenerator.getInstance();
		Symbol symbol = (Symbol)root.getAttribute(NodeKey.SYMBOL);
		switch (production) {
		case SyntaxProductionInit.Specifiers_DeclList_Semi_TO_Def:
			 Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
			 if (declarator != null) {
				 if (symbol.getSpecifierByType(Specifier.STRUCTURE) == null) {
						//如果是结构体数组，这里不做处理
					    generator.createArray(symbol);
				 }
			 } else {
				 //change here 遇到变量定义时，自动将其初始化为0
				 int i = generator.getLocalVariableIndex(symbol);
				 generator.emit(Instruction.SIPUSH, ""+0);
				 generator.emit(Instruction.ISTORE, ""+i);
			 }
			 
			 break; 
		}
		
		return root;
	}

}
