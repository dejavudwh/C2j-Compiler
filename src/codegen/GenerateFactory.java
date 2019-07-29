package codegen;

import ast.AstNode;
import ast.NodeKey;
import lexer.*;

public class GenerateFactory {
    private static GenerateFactory generateFactory = null;
    private GenerateFactory() {
    	
    }
    
    public static GenerateFactory getGenerateFactory() {
    	if (generateFactory == null) {
    		generateFactory = new GenerateFactory();
    	}
    	
    	return generateFactory;
    }
    
    public Generate getExecutor(AstNode node) {
    	
    	if (node == null) {
    		return null;
    	}
    	
    	Token type = (Token) node.getAttribute(NodeKey.TokenType);
    	switch (type) {
    	//change here
    	case DEF:
    		return new DefGenerate();
    	case DEF_LIST:
    		return new DefListGenerate();
    	case LOCAL_DEFS:
    	    return new LocalDefGenerate();
    	
    	case UNARY:
    		return new UnaryNodeGenerate();
    	case BINARY:
    		return new BinaryGenerate();
    	case NO_COMMA_EXPR:
    		return new NoCommaExprGenerate();
    	case EXPR:
    		return new ExprGenerate();
    	case STATEMENT:
    		return new StatementGenerate();
    	case STMT_LIST:
    		return new StatementListGenerate();
    	case TEST:
    		return new TestGenerate();
    	case IF_STATEMENT:
    		return new IfStatementGenerate();
    	case IF_ELSE_STATEMENT:
    		return new ElseStatementGenerate();
    		
    	case OPT_EXPR:
    		return new OptExprGenerate();
    		
    	case END_OPT_EXPR:
    		return new EndOptGenerate();
    		
    	case INITIALIZER:
    		return new InitializerGenerate();
    		
    	case COMPOUND_STMT:
    		return new CompoundStmtGenerate();
    		
    	case FUNCT_DECL:
    		return new FunctDeclGenerate();
    		
    	case EXT_DEF:
    		return new ExtDefGenerate();
    		
    	case ARGS:
    		return new ArgsGenerate();

		case RELOP:
			return new RelOpGenerate();
    	}
    	
    	return null;
    }
}
