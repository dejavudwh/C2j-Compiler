package debug;

import lexer.Lexer;
import parse.*;


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
//        snm.buildTransitionStateMachine();
    }

    private void testParse() throws Exception {
        ProductionManager pm = ProductionManager.getInstance();
        StateNodeManager snm = StateNodeManager.getInstance();
        snm.buildTransitionStateMachine();
        LRStateTableParser lrtp = new LRStateTableParser(new Lexer());
        lrtp.parse();
    }

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.testParse();
    }
}
