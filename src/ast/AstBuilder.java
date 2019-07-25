package ast;

import lexer.Token;
import parse.LRStateTableParser;
import parse.SyntaxProductionInit;
import symboltable.Symbol;
import symboltable.TypeSystem;

import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author dejavudwh isHudw
 */

public class AstBuilder {
    private Stack<AstNode> nodeStack = new Stack<>();

    private LRStateTableParser parser = null;
    private TypeSystem typeSystem = null;
    private Stack<Object> valueStack = null;
    private String functionName;
    private HashMap<String, AstNode> funcMap = new HashMap<>();

    private static AstBuilder instance;

    public static AstBuilder getInstance() {
        if (instance == null) {
            instance = new AstBuilder();
        }

        return instance;
    }

    public AstNode buildSyntaxTree(int production, String text) {
        AstNode node = null;
        Symbol symbol = null;
        AstNode child = null;

        switch (production) {
            case SyntaxProductionInit.Number_TO_Unary:
            case SyntaxProductionInit.Name_TO_Unary:
            case SyntaxProductionInit.String_TO_Unary:
                node = NodeFactory.createICodeNode(Token.UNARY);
                if (production == SyntaxProductionInit.Name_TO_Unary) {
                    assignSymbolToNode(node, text);
                }

                node.setAttribute(NodeKey.TEXT, text);
                break;

            case SyntaxProductionInit.Unary_LP_RP_TO_Unary:
                node = NodeFactory.createICodeNode(Token.UNARY);
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.Unary_LP_ARGS_RP_TO_Unary:
                node = NodeFactory.createICodeNode(Token.UNARY);
                node.addChild(nodeStack.pop());
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.Unary_Incop_TO_Unary:
            case SyntaxProductionInit.Unary_DecOp_TO_Unary:
            case SyntaxProductionInit.LP_Expr_RP_TO_Unary:
            case SyntaxProductionInit.Start_Unary_TO_Unary:
                node = NodeFactory.createICodeNode(Token.UNARY);
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.Unary_LB_Expr_RB_TO_Unary:
                node = NodeFactory.createICodeNode(Token.UNARY);
                node.addChild(nodeStack.pop());  //EXPR
                node.addChild(nodeStack.pop());  //UNARY
                break;

            case SyntaxProductionInit.Uanry_TO_Binary:
                node = NodeFactory.createICodeNode(Token.BINARY);
                child = nodeStack.pop();
                node.setAttribute(NodeKey.TEXT, child.getAttribute(NodeKey.TEXT));
                node.addChild(child);
                break;

            case SyntaxProductionInit.Binary_TO_NoCommaExpr:
            case SyntaxProductionInit.NoCommaExpr_Equal_NoCommaExpr_TO_NoCommaExpr:
                node = NodeFactory.createICodeNode(Token.NO_COMMA_EXPR);
                child = nodeStack.pop();
                String t = (String)child.getAttribute(NodeKey.TEXT);
                node.addChild(child);
                if (production == SyntaxProductionInit.NoCommaExpr_Equal_NoCommaExpr_TO_NoCommaExpr) {
                    child = nodeStack.pop();
                    t = (String)child.getAttribute(NodeKey.TEXT);
                    node.addChild(child);
                }
                break;

            case SyntaxProductionInit.Binary_Plus_Binary_TO_Binary:
            case SyntaxProductionInit.Binary_DivOp_Binary_TO_Binary:
            case SyntaxProductionInit.Binary_Minus_Binary_TO_Binary:
                node = NodeFactory.createICodeNode(Token.BINARY);
                node.addChild(nodeStack.pop());
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.Binary_RelOP_Binary_TO_Binray:
                node = NodeFactory.createICodeNode(Token.BINARY);
                node.addChild(nodeStack.pop());

                AstNode operator = NodeFactory.createICodeNode(Token.RELOP);
                operator.setAttribute(NodeKey.TEXT, parser.getRelOperatorText());
                node.addChild(operator);

                node.addChild(nodeStack.pop());

                break;

            case SyntaxProductionInit.NoCommaExpr_TO_Expr:
                node = NodeFactory.createICodeNode(Token.EXPR);
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.Expr_Semi_TO_Statement:
            case SyntaxProductionInit.CompountStmt_TO_Statement:
                node = NodeFactory.createICodeNode(Token.STATEMENT);
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.LocalDefs_TO_Statement:
                node = NodeFactory.createICodeNode(Token.STATEMENT);
                break;

            case SyntaxProductionInit.Statement_TO_StmtList:
                node = NodeFactory.createICodeNode(Token.STMT_LIST);
                if (nodeStack.size() > 0) {
                    node.addChild(nodeStack.pop());
                }
                break;

            case SyntaxProductionInit.FOR_OptExpr_Test_EndOptExpr_Statement_TO_Statement:
                node = NodeFactory.createICodeNode(Token.STATEMENT);
                node.addChild(nodeStack.pop());
                node.addChild(nodeStack.pop());
                node.addChild(nodeStack.pop());
                node.addChild(nodeStack.pop());
                break;
            default:
                break;
        }

        if (node != null) {
            node.setAttribute(NodeKey.PRODUCTION, production);
            nodeStack.push(node);
        }

        return node;
    }

    private Symbol assignSymbolToNode(AstNode node, String text) {
        Symbol symbol = typeSystem.getSymbolByText(text, parser.getCurrentLevel(), parser.symbolScope);
        node.setAttribute(NodeKey.SYMBOL, symbol);
        node.setAttribute(NodeKey.TEXT, text);

        return symbol;
    }

}
