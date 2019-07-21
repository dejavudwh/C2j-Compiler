package parse;

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
}
