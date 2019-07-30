package debug;

import ast.AstBuilder;
import interpreter.Interpreter;
import lexer.Lexer;
import parse.*;
import start.Start;


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

    private void testParse() {
        StateNodeManager snm = StateNodeManager.getInstance();
        snm.buildTransitionStateMachine();
        LRStateTableParser lrtp = new LRStateTableParser(new Lexer());
        lrtp.parse();
    }

    private void testCodegen() {

    }

    public static void main(String[] args) {
        Start start = new Start();
        start.start();
    }
}
