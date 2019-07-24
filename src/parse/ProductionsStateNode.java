package parse;

import debug.ConsoleDebugColor;
import lexer.Token;

import java.util.*;

/**
 * LR finite state automaton node
 * @author dejavudwh isHudw
 */

public class ProductionsStateNode {
    public static int stateNumCount = 0;
    /** Automaton state node number */
    public int stateNum;
    /** production of state node */
    public ArrayList<Production> productions;
    public ArrayList<Production> mergedProduction = new ArrayList<>();
    /** Node generated closures */
    private ArrayList<Production> closureSet = new ArrayList<>();
    /** Finish the closure generation partition */
    private HashMap<Integer, ArrayList<Production>> partition = new HashMap<>();
    private HashMap<Integer, ProductionsStateNode> transition = new HashMap<Integer, ProductionsStateNode>();

    private boolean transitionDone = false;

    private ProductionManager productionManager = ProductionManager.getInstance();
    private StateNodeManager stateNodeManager = StateNodeManager.getInstance();

    public ProductionsStateNode(ArrayList<Production> productions) {
        this.stateNum = stateNumCount;
        this.productions = productions;
        this.closureSet.addAll(this.productions);
    }

    public static void  increaseStateNum() {
        stateNumCount++;
    }

    public void buildTransition() {
        if (transitionDone) {
            return;
        }
        transitionDone = true;

        /** Closure of a node's production */
        makeClosure();
        partition();
        makeTransition();
    }

    private void makeClosure() {
        ConsoleDebugColor.outlnPurple("==== state begin make closure sets ====");

        Stack<Production> productionStack = new Stack<>();
        for (Production production : productions) {
            productionStack.push(production);
        }

        while (!productionStack.isEmpty()) {
            Production production = productionStack.pop();

            ConsoleDebugColor.outlnPurple("production on top of stack is : ");
            production.debugPrint();
            production.debugPrintBeta();

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
//                    ConsoleDebugColor.outlnPurple("push and add new production to stack and closureSet");

                    closureSet.add(newProduct);
                    productionStack.push(newProduct);
//                    ConsoleDebugColor.outlnPurple("Add new production:");
//                    newProduct.debugPrint();
                    removeRedundantProduction(newProduct);
                } else {
                    ConsoleDebugColor.outlnPurple("the production is already exist!");
                }
            }
        }

        debugPrintClosure();
        ConsoleDebugColor.outlnPurple("==== make closure sets end ====");
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
//                    ConsoleDebugColor.outlnPurple("remove redundant production: ");
//                    item.debugPrint();
                    break;
                }
            }
        }
    }

    private void partition() {
        ConsoleDebugColor.outlnPurple("==== state begin make partition ====");

        for (Production production : closureSet) {
            int symbol = production.getDotSymbol();
            if (symbol == Token.UNKNOWN_TOKEN.ordinal()) {
                continue;
            }

            ArrayList<Production> productionList = partition.get(symbol);
            if (productionList == null) {
                productionList = new ArrayList<>();
                partition.put(production.getDotSymbol(), productionList);
            }

            if (!productionList.contains(production)) {
                productionList.add(production);
            }
        }

//        debugPrintPartition();
//        ConsoleDebugColor.outlnPurple("==== make partition end ====");
    }

    private void makeTransition() {
        for (Map.Entry<Integer, ArrayList<Production>> entry : partition.entrySet()) {
            ProductionsStateNode nextState = makeNextStateNode(entry.getKey());
            ConsoleDebugColor.outlnPurple("==== begin make transition info ====");

            transition.put(entry.getKey(), nextState);

            ConsoleDebugColor.outlnPurple("from state " + stateNum + " to State " + nextState.stateNum + " on " +
            Token.getTokenStr(entry.getKey()));

            ConsoleDebugColor.outlnPurple("==== State " + nextState.stateNum + "====");
            nextState.debugPrint();

            stateNodeManager.addTransition(this, nextState, entry.getKey());
        }

//        debugPrintTransition();

        extendFollowingTransition();
    }

    private ProductionsStateNode makeNextStateNode(int left) {
        ArrayList<Production> productions = partition.get(left);
        ArrayList<Production> newProductions = new ArrayList<>();

        for (int i = 0; i < productions.size(); i++) {
            Production production = productions.get(i);
            newProductions.add(production.dotForward());
        }

        return stateNodeManager.getStateNode(newProductions);
    }

    private void extendFollowingTransition() {
        for (Map.Entry<Integer, ProductionsStateNode> entry : transition.entrySet()) {
            ProductionsStateNode state = entry.getValue();
            if (!state.isTransitionDone()) {
                state.buildTransition();
            }
        }
    }

    public boolean isTransitionDone() {
        return transitionDone;
    }

    public void stateMerge(ProductionsStateNode node) {
        if (!this.productions.contains(node.productions)) {
            for (int i = 0; i < node.productions.size(); i++) {
                if (!this.productions.contains(node.productions.get(i))
                        && !mergedProduction.contains(node.productions.get(i))
                ) {
                    mergedProduction.add(node.productions.get(i));
                }
            }
        }
    }

    public HashMap<Integer, Integer> makeReduce() {
        HashMap<Integer, Integer> map = new HashMap<>();
        reduce(map, this.productions);
        reduce(map, this.mergedProduction);

        return map;
    }

    private void reduce(HashMap<Integer, Integer> map, ArrayList<Production> productions) {
        for (int i = 0; i < productions.size(); i++) {
            if (productions.get(i).canBeReduce()) {
                ArrayList<Integer> lookAhead = productions.get(i).getLookAheadSet();
                for (int j = 0; j < lookAhead.size(); j++) {
                    map.put(lookAhead.get(j), (productions.get(i).getProductionNum()));
                }
            }
        }
    }

    public void debugPrint() {
        if (ConsoleDebugColor.DEBUG) {
            System.out.println("State Number: " + stateNum);
            for (int i = 0; i < productions.size(); i++) {
                productions.get(i).debugPrint();
            }

            for (int i = 0; i < mergedProduction.size(); i++) {
                mergedProduction.get(i).debugPrint();
            }
        }
    }

    private void debugPrintClosure() {
        ConsoleDebugColor.outlnPurple("Closure Sets is: ");
        for (int i = 0; i < closureSet.size(); i++) {
            closureSet.get(i).debugPrint();
        }
    }

    private void debugPrintPartition() {
        for(Map.Entry<Integer, ArrayList<Production>> entry : partition.entrySet()) {

            ConsoleDebugColor.outlnPurple("partition for symbol: " + Token.getTokenStr(entry.getKey()));

            ArrayList<Production> productionList = entry.getValue();
            for (int i = 0; i < productionList.size(); i++) {
                productionList.get(i).debugPrint();
            }
        }
    }

    public void debugPrintTransition() {
        if (ConsoleDebugColor.DEBUG) {
            for (Map.Entry<Integer, ProductionsStateNode> entry : transition.entrySet()) {
                ConsoleDebugColor.outlnPurple("transfer on " + Token.getTokenStr(entry.getKey()) + " to state ");
                entry.getValue().debugPrint();
                System.out.print("\n");
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return checkProductionEqual(obj, false);
    }

    public boolean checkProductionEqual(Object obj, boolean isPartial) {
        ProductionsStateNode node = (ProductionsStateNode) obj;

        if (node.productions.size() != this.productions.size()) {
            return false;
        }

        int equalCount = 0;

        for (int i = 0; i < node.productions.size(); i++) {
            for (int j = 0; j < this.productions.size(); j++) {
                if (!isPartial) {
                    if (node.productions.get(i).equals(this.productions.get(j))) {
                        equalCount++;
                        break;
                    }
                } else {
                    if (node.productions.get(i).productionEquals(this.productions.get(j))) {
                        equalCount++;
                        break;
                    }
                }
            }
        }

        return equalCount == node.productions.size();
    }

}
