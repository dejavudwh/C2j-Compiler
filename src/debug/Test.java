package debug;

import parse.FirstSetBuilder;
import parse.Production;
import parse.ProductionManager;
import parse.ProductionsStateNode;

import java.util.ArrayList;

/**
 *
 * @author dejavudwh isHudw
 */

public class Test {
    private void testFirstSets() {
        FirstSetBuilder fsb = new FirstSetBuilder();
        fsb.buildFirstSets();
    }

    private void testBuildTransition() {
        ProductionManager pm = ProductionManager.getInstance();
        ArrayList<Production> p = pm.getProduction(0);
        ProductionsStateNode psn = new ProductionsStateNode(p);
        psn.buildTransition();
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.testBuildTransition();
    }
}
