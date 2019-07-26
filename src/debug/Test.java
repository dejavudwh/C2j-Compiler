package debug;

import ast.AstBuilder;
import interpreter.Interpreter;
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

    private void testParse() {
        StateNodeManager snm = StateNodeManager.getInstance();
        snm.buildTransitionStateMachine();
        LRStateTableParser lrtp = new LRStateTableParser(new Lexer());
        lrtp.parse();
    }

    private void testInterpreter() {
        StateNodeManager snm = StateNodeManager.getInstance();
        snm.buildTransitionStateMachine();
        LRStateTableParser lrtp = new LRStateTableParser(new Lexer());
        lrtp.parse();
        Interpreter interpreter = Interpreter.getInstance();
        AstBuilder astBuilder = AstBuilder.getInstance();
        interpreter.execute(astBuilder.getSyntaxTreeRoot());
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.testInterpreter();
    }
}
