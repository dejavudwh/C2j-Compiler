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

        return product;
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

    public ArrayList<Integer> computeFirstSetOfBetaAndc() {
        ArrayList<Integer> set = new ArrayList<>();
        for (int i = dotPos + 1; i < right.size(); i++) {
            set.add(right.get(i));
        }

        ProductionManager manager = ProductionManager.getInstance();
        ArrayList<Integer> firstSet = new ArrayList<>();

        if (set.size() > 0) {
            for (int i = 0; i < set.size(); i++) {
                ArrayList<Integer> lookAhead = manager.getFirstSetBuilder().getFirstSet(set.get(i));

                for (int s : lookAhead) {
                    if (!firstSet.contains(s)) {
                        firstSet.add(s);
                    }
                }

                if (!manager.getFirstSetBuilder().isSymbolNullable(set.get(i))) {
                    break;
                }

                if (i == lookAhead.size() - 1) {
                    //beta is composed by nulleable terms
                    firstSet.addAll(this.lookAhead);
                }            }
        } else {
            firstSet.addAll(lookAhead);
        }

        return firstSet;
    }

    public Production cloneSelf() {
        Production product = new Production(productionNum, this.left, dotPos, this.right);

        product.lookAhead = new ArrayList<>();
        product.lookAhead.addAll(this.lookAhead);

        return product;
    }

    public void addLookAheadSet(ArrayList<Integer> lookAhead) {
        this.lookAhead = lookAhead;
    }

    @Override
    public boolean equals(Object obj) {
        Production production = (Production) obj;

        return productionEquals(production) && lookAheadSetComparing(production) == 0;
    }

    public boolean isCover(Production product) {
        return productionEquals(product) && lookAheadSetComparing(product) > 0;
    }

    public boolean productionEquals(Production product) {
        if (this.left != product.getLeft()) {
            return false;
        }

        if (!this.right.equals(product.getRight())) {
            return false;
        }

        if (this.dotPos != product.getDotPos()) {
            return false;
        }

        return true;
    }

    public int lookAheadSetComparing(Production product) {
        if (this.lookAhead.size() > product.lookAhead.size()) {
            for (int i = 0; i < product.lookAhead.size(); i++) {
                if (!this.lookAhead.contains(product.lookAhead.get(i))) {
                    return -1;
                }
            }
            return 1;
        } else if (this.lookAhead.size() < product.lookAhead.size()) {
            return -1;
        } else if (this.lookAhead.size() == product.lookAhead.size()) {
            for (int i = 0; i < this.lookAhead.size(); i++) {
                if (!this.lookAhead.get(i).equals(product.lookAhead.get(i))) {
                    return -1;
                }
            }
        }

        return 0;
    }

    public void debugPrint() {
        if (ConsoleDebugColor.DEBUG) {
            ConsoleDebugColor.outBlue(Token.getTokenStr(left) + " -> ");
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
    }

    public void debugPrintBeta() {
        if (ConsoleDebugColor.DEBUG) {
            System.out.print("Beta part of production is: ");
            for (int i = dotPos + 1; i < right.size(); i++) {
                int val = right.get(i);
                ConsoleDebugColor.outCyan(Token.values()[val].toString());
            }

            if (dotPos + 1 >= right.size()) {
                ConsoleDebugColor.outCyan("null");
            }

            System.out.println();
        }
    }

}
