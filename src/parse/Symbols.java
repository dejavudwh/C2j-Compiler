package parse;

import lexer.Token;
import sun.awt.Symbol;

import java.util.ArrayList;

/**
 * The sign of production
 * @author dejavudwh isHudw
 */
public class Symbols {
    public int value;
    public ArrayList<int[]> productions;
    public ArrayList<Integer> firstSet = new ArrayList<>();
    public ArrayList<Integer> followSet = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> selectionSet = new ArrayList<>();
    public boolean isNullable;

    public Symbols(int value, boolean isNullable, ArrayList<int[]> productions) {
        this.value = value;
        this.productions =  productions;
        this.isNullable = isNullable;

        if (Token.isTerminal(value)) {
            //terminal's first set is itself
            firstSet.add(value);
        }
    }

    public void addProduction(int[] production) {
        if (!productions.contains(production)) {
            productions.add(production);
        }
    }
}
