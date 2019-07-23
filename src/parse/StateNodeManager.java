package parse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author dejavudwh isHudw
 */

public class StateNodeManager {
    private static StateNodeManager instance;

    private ArrayList<ProductionsStateNode> stateList = new ArrayList<>();
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
        HashMap<Integer, ProductionsStateNode> map = transitionMap.get(from);
        if (map == null) {
            map = new HashMap<>();
        }

        map.put(on, to);
        transitionMap.put(from, map);
    }
}
