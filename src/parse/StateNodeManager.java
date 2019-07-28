package parse;

import debug.ConsoleDebugColor;
import lexer.Token;

import java.io.*;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author dejavudwh isHudw
 */

public class StateNodeManager {
    private static StateNodeManager instance;

    private ArrayList<ProductionsStateNode> stateList = new ArrayList<>();
    private boolean isTransitionTableCompressed = true;
    private ArrayList<ProductionsStateNode> compressedStateList = new ArrayList<>();
    private HashMap<ProductionsStateNode, HashMap<Integer, ProductionsStateNode>> transitionMap = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, Integer>> lrStateTable = new HashMap<>();

    private StateNodeManager() {
    }

    public static StateNodeManager getInstance() {
        if (instance == null) {
            instance = new StateNodeManager();
        }

        return instance;
    }

    public void buildTransitionStateMachine() {
        File table = new File("lrStateTable.sb");
        if (table.exists()) {
            return;
        }
        ProductionManager productionManager = ProductionManager.getInstance();
        productionManager.buildFirstSets();
        ProductionsStateNode state = getStateNode(productionManager.getProduction(Token.PROGRAM.ordinal()));

        state.buildTransition();

        debugPrintStateMap();
    }

    public ProductionsStateNode getStateNode(ArrayList<Production> productions) {
        ProductionsStateNode node = new ProductionsStateNode(productions);

        if (!stateList.contains(node)) {
            stateList.add(node);
            ProductionsStateNode.increaseStateNum();
            return node;
        }

        for (ProductionsStateNode sn : stateList) {
            if (sn.equals(node)) {
                node = sn;
            }
        }

        return node;
    }

    public void addTransition(ProductionsStateNode from, ProductionsStateNode to, int on) {
        /* Compress the finite state machine nodes */
        if (isTransitionTableCompressed) {
            from = getAndMergeSimilarStates(from);
            to = getAndMergeSimilarStates(to);
        }

        HashMap<Integer, ProductionsStateNode> map = transitionMap.get(from);
        if (map == null) {
            map = new HashMap<>();
        }

        map.put(on, to);
        transitionMap.put(from, map);
    }

    private ProductionsStateNode getAndMergeSimilarStates(ProductionsStateNode node) {
        Iterator<ProductionsStateNode> it = stateList.iterator();
        ProductionsStateNode currentNode = null, returnNode = node;

        while (it.hasNext()) {
            currentNode = it.next();

            if (!currentNode.equals(node) && currentNode.checkProductionEqual(node, true)) {
                if (currentNode.stateNum < node.stateNum) {
                    currentNode.stateMerge(node);
                    returnNode = currentNode;
                } else {
                    node.stateMerge(currentNode);
                    returnNode = node;
                }
                break;
            }
        }

        if (!compressedStateList.contains(returnNode)) {
            compressedStateList.add(returnNode);
        }

        return returnNode;

    }

    public HashMap<Integer, HashMap<Integer, Integer>> getLrStateTable() {
        File table = new File("lrStateTable.sb");
        if (table.exists()) {
            return loadTable();
        }

        Iterator it;
        if (isTransitionTableCompressed) {
            it = compressedStateList.iterator();
        } else {
            it = stateList.iterator();
        }

        while (it.hasNext()) {
            ProductionsStateNode state = (ProductionsStateNode) it.next();
            HashMap<Integer, ProductionsStateNode> map = transitionMap.get(state);
            HashMap<Integer, Integer> jump = new HashMap<>();

            if (map != null) {
                for (Map.Entry<Integer, ProductionsStateNode> item : map.entrySet()) {
                    jump.put(item.getKey(), item.getValue().stateNum);
                }
            }

            HashMap<Integer, Integer> reduceMap = state.makeReduce();
            if (reduceMap.size() > 0) {
                for (Map.Entry<Integer, Integer> item : reduceMap.entrySet()) {

                    jump.put(item.getKey(), -(item.getValue()));
                }
            }

            lrStateTable.put(state.stateNum, jump);
        }

        storageTableToFile(lrStateTable);

        return lrStateTable;
    }

    private void storageTableToFile(HashMap<Integer, HashMap<Integer, Integer>> lrStateTable) {
        try {
            ConsoleDebugColor.outlnPurple("start.Start writing the LR state analysis table to a file");

            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("lrStateTable.sb"));
            oos.writeObject(lrStateTable);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<Integer, HashMap<Integer, Integer>> loadTable() {
        try {
            ConsoleDebugColor.outlnPurple("Reads the state analysis table from the file");

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("lrStateTable.sb"));

            return (HashMap<Integer, HashMap<Integer, Integer>>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void debugPrintStateMap() {
        if (ConsoleDebugColor.DEBUG) {
            ConsoleDebugColor.outlnPurple("Map size is: " + transitionMap.size());

            for (Map.Entry<ProductionsStateNode, HashMap<Integer, ProductionsStateNode>> entry : transitionMap.entrySet()) {
                ProductionsStateNode from = entry.getKey();
                ConsoleDebugColor.outlnPurple("********Status node information********");
                ConsoleDebugColor.outlnPurple("from state: ");
                from.debugPrint();

                HashMap<Integer, ProductionsStateNode> map = entry.getValue();
                for (Map.Entry<Integer, ProductionsStateNode> item : map.entrySet()) {
                    int symbol = item.getKey();
                    ConsoleDebugColor.outlnPurple("on Symbol: " + Token.getTokenStr(symbol));
                    ConsoleDebugColor.outlnPurple("to state: ");
                    ProductionsStateNode to = item.getValue();
                    to.debugPrint();
                }

                ConsoleDebugColor.outlnPurple("********end state machine********");
            }
        }
    }

}