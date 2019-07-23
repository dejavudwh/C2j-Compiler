package debug;

import lexer.Token;
import parse.*;

import javax.swing.plaf.nimbus.State;
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

    private void buildTransitionStateMachine() {
        StateNodeManager snm = StateNodeManager.getInstance();
        snm.buildTransitionStateMachine();
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.buildTransitionStateMachine();
    }
}
