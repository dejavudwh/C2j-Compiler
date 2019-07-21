package parse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * LR finite state automaton node
 * @author dejavudwh isHudw
 */

public class ProductionsStateNode {
    public static int stateNumCount = 0;
    /** Automaton state node number */
    private int stateNum;
    /** production of state node */
    private ArrayList<Production> productions = new ArrayList<>();
    /** Node generated closures */
    private ArrayList<Production> closureSet = new ArrayList<>();
    /** Finish the closure generation partition */
    private HashMap<Integer, ArrayList<Production>> partition = new HashMap<>();

    public ProductionsStateNode(ArrayList<Production> productions) {
        this.stateNum = stateNumCount;

        this.productions = productions;

        this.closureSet.addAll(this.productions);
        increaseStateNum();
    }

    private static void  increaseStateNum() {
        stateNumCount++;
    }
}
