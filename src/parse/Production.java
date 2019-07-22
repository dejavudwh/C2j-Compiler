package parse;

import debug.ConsoleDebugColor;
import lexer.Token;

import java.util.ArrayList;

/**
 * Syntax generated classes
 * @author dejavudwh isHudw
 */

public class Production {
    private int dotPos = 0;
    private int left;
    private ArrayList<Integer> right;
    private ArrayList<Integer> lookAhead = new ArrayList<>();
    private int productionNum = -1;

    public Production(int productionNum, int left, int dot, ArrayList<Integer> right) {
        this.left = left;
        this.right = right;
        this.productionNum = productionNum;
        lookAhead.add(Token.SEMI.ordinal());

        if (dot >= right.size()) {
            dot = right.size();
        }
        this.dotPos = dot;
    }

    public Production dotForward() {
        Production product = new Production(productionNum, this.left, dotPos+1, this.right);
        product.lookAhead = new ArrayList<>();
        product.lookAhead.addAll(this.lookAhead);

        return  product;
    }

    public int getLeft() {
        return left;
    }

    public ArrayList<Integer> getRight() {
        return right;
    }

    public int getDotPos() {
        return dotPos;
    }

    public int getDotSymbol() {
        if (dotPos >= right.size()) {
            return Token.UNKNOWN_TOKEN.ordinal();
        }

        return right.get(dotPos);
    }

    public void print() {
        ConsoleDebugColor.outBlue(Token.getTokenStr(left) + " -> " );
        boolean printDot = false;
        for (int i = 0; i < right.size(); i++) {
            if (i == dotPos) {
                printDot = true;
                ConsoleDebugColor.outGreen(".");
            }

            ConsoleDebugColor.outCyan(Token.getTokenStr(right.get(i)) + " ");
        }

        if (!printDot) {
            ConsoleDebugColor.outGreen(".");
        }

        System.out.print("look ahead set: { ");
        for (int i = 0; i < lookAhead.size(); i++) {
            System.out.print(Token.getTokenStr(lookAhead.get(i)) + " ");
        }
        System.out.println("}");
    }

    public void printBeta() {
        System.out.print("Beta part of production is: ");
        for (int i = dotPos + 1; i < right.size(); i++) {
            //System.out.print(SymbolDefine.getSymbolStr(right.get(i)) + " ");
            int val = right.get(i);
            ConsoleDebugColor.outCyan(Token.values()[val].toString());
        }

        if (dotPos+1 >= right.size()) {
            ConsoleDebugColor.outCyan("null");
        }

        System.out.println();
    }

}
