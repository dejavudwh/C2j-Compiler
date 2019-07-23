package parse;

import debug.ConsoleDebugColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    private StateNodeManager() {}

    public static StateNodeManager getInstance() {
        if (instance == null) {
            instance = new StateNodeManager();
        }

        return instance;
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
            to   = getAndMergeSimilarStates(to);
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
                }
                else {
                    node.stateMerge(currentNode);
                    returnNode = node;
                }
                break;
            }
        }

        if (compressedStateList.contains(returnNode)) {
            compressedStateList.add(returnNode);
        }

        return returnNode;

    }
}
