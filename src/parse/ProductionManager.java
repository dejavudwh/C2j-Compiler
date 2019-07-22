package parse;

import java.util.ArrayList;
import java.util.HashMap;

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

}
