package parse;

import debug.ConsoleDebugColor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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
    private boolean transitionDone = false;

    public ProductionsStateNode(ArrayList<Production> productions) {
        this.stateNum = stateNumCount;
        this.productions = productions;
        this.closureSet.addAll(this.productions);
        increaseStateNum();
    }

    private static void  increaseStateNum() {
        stateNumCount++;
    }

    public void buildTransition() {
        if (transitionDone) {
            return;
        }
        transitionDone = true;

        /** Closure of a node's production */
        makeClosure();

    }

    private void makeClosure() {
        Stack<Production> productionStack = new Stack<>();
        for (Production production : productions) {
            productionStack.push(production);
        }

        while (!productionStack.isEmpty()) {
            Production production = productionStack.pop();

            if (ConsoleDebugColor.DEBUG) {
                ConsoleDebugColor.outPurple("production on top of stack is : ");
                production.print();
                production.printBeta();
            }
        }
    }
}
