package parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dejavudwh isHudw
 */

public class ProductionManager {
    private static ProductionManager instance;

    private FirstSetBuilder firstSetBuilder = new FirstSetBuilder();
    private HashMap<Integer, ArrayList<Production>> productionMap = new HashMap<>();

    private ProductionManager() {
        initialize();
    }

    public static ProductionManager getInstance() {
        if (instance == null) {
            instance = new ProductionManager();
        }

        return instance;
    }

    private void initialize() {
        SyntaxProductionInit syntaxProductionInit = SyntaxProductionInit.getInstance();
        productionMap = syntaxProductionInit.getProductionMap();
        firstSetBuilder.buildFirstSets();
    }

    public FirstSetBuilder getFirstSetBuilder() {
        return firstSetBuilder;
    }

    public ArrayList<Production> getProduction(int symbol) {
        return productionMap.get(symbol);
    }

    public Production getProductionByIndex(int index) {

        for (Map.Entry<Integer, ArrayList<Production>> item : productionMap.entrySet()) {
            ArrayList<Production> productionList = item.getValue();
            for (int i = 0; i < productionList.size(); i++) {
                if (productionList.get(i).getProductionNum() == index) {
                    return productionList.get(i);
                }
            }
        }

        return null;
    }

}
