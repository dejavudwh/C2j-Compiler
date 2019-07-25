package ast;

import lexer.Token;
import parse.LRStateTableParser;
import parse.SyntaxProductionInit;
import symboltable.Symbol;
import symboltable.TypeSystem;

import java.util.HashMap;
import java.util.Stack;

/**
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

    public void setParser(LRStateTableParser parser) {
        this.parser = parser;
        typeSystem = parser.getTypeSystem();
        valueStack = parser.getValueStack();
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
                String t = (String) child.getAttribute(NodeKey.TEXT);
                node.addChild(child);
                if (production == SyntaxProductionInit.NoCommaExpr_Equal_NoCommaExpr_TO_NoCommaExpr) {
                    child = nodeStack.pop();
                    t = (String) child.getAttribute(NodeKey.TEXT);
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

            case SyntaxProductionInit.StmtList_Statement_TO_StmtList:
                node = NodeFactory.createICodeNode(Token.STMT_LIST);
                node.addChild(nodeStack.pop());
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.Expr_TO_Test:
                node = NodeFactory.createICodeNode(Token.TEST);
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.If_Test_Statement_TO_IFStatement:
                node = NodeFactory.createICodeNode(Token.IF_STATEMENT);
                node.addChild(nodeStack.pop()); //Test
                node.addChild(nodeStack.pop()); //Statement
                break;

            case SyntaxProductionInit.IfElseStatemnt_Else_Statemenet_TO_IfElseStatement:
                node = NodeFactory.createICodeNode(Token.IF_ELSE_STATEMENT);
                node.addChild(nodeStack.pop()); //IfStatement
                node.addChild(nodeStack.pop()); // statement
                break;

            case SyntaxProductionInit.While_LP_Test_Rp_TO_Statement:
            case SyntaxProductionInit.Do_Statement_While_Test_To_Statement:
                node = NodeFactory.createICodeNode(Token.STATEMENT);
                node.addChild(nodeStack.pop());
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.Expr_Semi_TO_OptExpr:
            case SyntaxProductionInit.Semi_TO_OptExpr:
                node = NodeFactory.createICodeNode(Token.OPT_EXPR);
                if (production == SyntaxProductionInit.Expr_Semi_TO_OptExpr) {
                    node.addChild(nodeStack.pop());
                }
                break;

            case SyntaxProductionInit.Expr_TO_EndOpt:
                node = NodeFactory.createICodeNode(Token.END_OPT_EXPR);
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.LocalDefs_StmtList_TO_CompoundStmt:
                node = NodeFactory.createICodeNode(Token.COMPOUND_STMT);
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.NewName_LP_RP_TO_FunctDecl:
            case SyntaxProductionInit.NewName_LP_VarList_RP_TO_FunctDecl:
                node = NodeFactory.createICodeNode(Token.FUNCT_DECL);
                node.addChild(nodeStack.pop());
                child = node.getChildren().get(0);
                functionName = (String) child.getAttribute(NodeKey.TEXT);
                symbol = assignSymbolToNode(node, functionName);

                break;

            case SyntaxProductionInit.NewName_TO_VarDecl:
                //Do not process variable declaration statements for the time being
                nodeStack.pop();
                break;

            case SyntaxProductionInit.NAME_TO_NewName:
                node = NodeFactory.createICodeNode(Token.NEW_NAME);
                node.setAttribute(NodeKey.TEXT, text);
                break;

            case SyntaxProductionInit.OptSpecifiers_FunctDecl_CompoundStmt_TO_ExtDef:
                node = NodeFactory.createICodeNode(Token.EXT_DEF);
                node.addChild(nodeStack.pop());
                node.addChild(nodeStack.pop());
                funcMap.put(functionName, node);
                break;

            case SyntaxProductionInit.NoCommaExpr_TO_Args:
                node = NodeFactory.createICodeNode(Token.ARGS);
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.NoCommaExpr_Comma_Args_TO_Args:
                node = NodeFactory.createICodeNode(Token.ARGS);
                node.addChild(nodeStack.pop());
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.Return_Semi_TO_Statement:
                node = NodeFactory.createICodeNode(Token.STATEMENT);
                break;

            case SyntaxProductionInit.Return_Expr_Semi_TO_Statement:
                node = NodeFactory.createICodeNode(Token.STATEMENT);
                node.addChild(nodeStack.pop());
                break;

            case SyntaxProductionInit.Unary_StructOP_Name_TO_Unary:
                node = NodeFactory.createICodeNode(Token.UNARY);
                node.addChild(nodeStack.pop());
                node.setAttribute(NodeKey.TEXT, text);
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

    public AstNode getFunctionNodeByName(String name) {
        return funcMap.get(name);
    }

    public AstNode getSyntaxTreeRoot() {
        AstNode mainNode = funcMap.get("main");
        return mainNode;
    }

}
