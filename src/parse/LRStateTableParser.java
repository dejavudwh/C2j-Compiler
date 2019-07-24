package parse;

import debug.ConsoleDebugColor;
import lexer.Lexer;
import lexer.Token;

import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author dejavudwh isHudw
 */

public class LRStateTableParser {
    private Lexer lexer;
    int lexerInput = 0;
    String text = "";

    private Stack<Integer> statusStack = new Stack<>();
    private Stack<Object> valueStack = new Stack<>();
    private Stack<Integer> parseStack = new Stack<>();
    private HashMap<Integer, HashMap<Integer, Integer>> lrStateTable = null;

    public LRStateTableParser(Lexer lexer) {
        this.lexer = lexer;
        statusStack.push(0);
        valueStack.push(null);
        lexer.advance();
        lexerInput = Token.EXT_DEF_LIST.ordinal();
        lrStateTable = StateNodeManager.getInstance().getLrStateTable();
    }

    public void parse() {
        while (true) {
            Integer action = getAction(statusStack.peek(), lexerInput);

            if (action == null) {
                System.out.println("Shift for input: " + Token.values()[lexerInput].toString());
                System.err.println("The input is denied");
                return;
            }

            if (action > 0) {
                statusStack.push(action);
                parseStack.push(lexerInput);

                if (Token.isTerminal(lexerInput)) {
                    System.out.println("Shift for input: " + Token.values()[lexerInput].toString());

                    lexer.advance();
                    lexerInput = lexer.lookAhead;
                } else {
                    lexerInput = lexer.lookAhead;
                }
            } else {
                if (action == 0) {
                    System.out.println("The input can be accepted");
                    return;
                }

                int reduceProduction = -action;
                Production product = ProductionManager.getInstance().getProductionByIndex(reduceProduction);
                System.out.println("reduce by product: ");
                product.debugPrint();

                int rightSize = product.getRight().size();
                while (rightSize > 0) {
                    parseStack.pop();
                    statusStack.pop();
                    rightSize--;
                }

                lexerInput = product.getLeft();
                parseStack.push(lexerInput);
            }
        }
    }

    private Integer getAction(Integer currentState, Integer currentInput) {
        HashMap<Integer, Integer> jump = lrStateTable.get(currentState);
        if (jump != null) {
            Integer next = jump.get(currentInput);
            if (next != null) {
                return next;
            }
        }

        return null;
    }
}
