package debug;

import lexer.Token;
import parse.*;

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
        ProductionsStateNode state = StateNodeManager.getInstance().getStateNode(pm.getProduction(Token.PROGRAM.ordinal()));
        state.buildTransition();
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.testBuildTransition();
    }
}
