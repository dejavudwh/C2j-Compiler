package parse;

import java.util.HashMap;

/**
 * Initialization of syntactic derivation
 * @author dejavudwh isHudw
 */

public class SyntaxProductionInit {
    private static SyntaxProductionInit instance = null;
    private HashMap<Integer, Production> productionMap = new HashMap<>();

    private SyntaxProductionInit() {}

    public SyntaxProductionInit getInstance() {
        if (instance == null) {
            instance = new SyntaxProductionInit();
        }

        return instance;
    }
}
