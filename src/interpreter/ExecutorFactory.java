package interpreter;

import ast.AstNode;
import ast.NodeKey;
import lexer.Token;

/**
 * @author dejavdwh isHudw
 */

public class ExecutorFactory {
    private static ExecutorFactory instance = null;

    private ExecutorFactory() { }

    public static ExecutorFactory getInstance() {
        if (instance == null) {
            instance = new ExecutorFactory();
        }

        return instance;
    }

    public Executor getExecutor(AstNode node) {
        if (node == null) {
            return null;
        }

        Token type = (Token) node.getAttribute(NodeKey.TokenType);
        switch (type) {
            case UNARY:
                return new UnaryNodeExecutor();

            case BINARY:
                return new BinaryExecutor();

            case NO_COMMA_EXPR:
                return new NoCommaExprExecutor();

            case EXPR:
                return new ExprExecutor();

            case STATEMENT:
                return new StatementExecutor();

            case STMT_LIST:
                return new StatementListExecutor();

            case TEST:
                return new TestExecutor();

            case IF_STATEMENT:
                return new IfStatementExecutor();

            case IF_ELSE_STATEMENT:
                return new ElseStatementExecutor();

            case OPT_EXPR:
                return new OptExprExecutor();

            case END_OPT_EXPR:
                return new EndOptExecutor();

            case INITIALIZER:
                return new InitializerExecutor();

            case COMPOUND_STMT:
                return new CompoundStmtExecutor();

            case FUNCT_DECL:
                return new FunctDeclExecutor();

            case EXT_DEF:
                return new ExtDefExecutor();

            case ARGS:
                return new ArgsExecutor();

            case RELOP:
                return new RelOpExecutor();

            default:
                break;
        }

        return null;
    }
}
