package parse;

import debug.ConsoleDebugColor;
import lexer.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private ArrayList<Production> productions;
    /** Node generated closures */
    private ArrayList<Production> closureSet = new ArrayList<>();
    /** Finish the closure generation partition */
    private HashMap<Integer, ArrayList<Production>> partition = new HashMap<>();
    private boolean transitionDone = false;

    private ProductionManager productionManager = ProductionManager.getInstance();

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
                ConsoleDebugColor.outlnPurple("production on top of stack is : ");
                production.print();
                production.printBeta();
            }

            if (Token.isTerminal(production.getDotSymbol())) {
                ConsoleDebugColor.outlnPurple("symbol after dot is not non-terminal, ignore and process next item");
                continue;
            }

            int symbol = production.getDotSymbol();
            ArrayList<Production> closures = productionManager.getProduction(symbol);
            ArrayList<Integer> lookAhead = production.computeFirstSetOfBetaAndc();

            Iterator<Production> it = closures.iterator();
            while (it.hasNext()) {
                Production oldProduct = it.next();
                Production newProduct = oldProduct.cloneSelf();

                newProduct.addLookAheadSet(lookAhead);
                if (!closureSet.contains(newProduct)) {
                    ConsoleDebugColor.outlnPurple("push and add new production to stack and closureSet");

                    closureSet.add(newProduct);
                    productionStack.push(newProduct);
                    ConsoleDebugColor.outlnPurple("Add new production:");
                    newProduct.print();
                    removeRedundantProduction(newProduct);
                } else {
                    ConsoleDebugColor.outlnPurple("the production is already exist!");
                }
            }

        }
    }

    private void removeRedundantProduction(Production product) {
        boolean removeHappended = true;

        while (removeHappended) {
            removeHappended = false;

            Iterator it = closureSet.iterator();
            while (it.hasNext()) {
                Production item = (Production) it.next();
                if (product.isCover(item)) {
                    removeHappended = true;
                    closureSet.remove(item);
                    ConsoleDebugColor.outlnPurple("remove redundant production: ");
                    item.print();
                    break;
                }
            }
        }
    }
}
