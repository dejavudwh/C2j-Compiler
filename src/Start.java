import ast.AstBuilder;
import interpreter.Interpreter;
import lexer.Lexer;
import parse.LRStateTableParser;
import parse.StateNodeManager;

public class Start {
    public static final int INTERPRETER = 1;
    public static final int CODEGEN = 2;

    public static int STARTTYPE = 1;

    public void interpreter() {
        StateNodeManager snm = StateNodeManager.getInstance();
        snm.buildTransitionStateMachine();
        LRStateTableParser tableParser = new LRStateTableParser(new Lexer());
        tableParser.parse();
        Interpreter interpreter = Interpreter.getInstance();
        AstBuilder astBuilder = AstBuilder.getInstance();
        interpreter.execute(astBuilder.getSyntaxTreeRoot());
    }

    public void codegen() {

    }

    public static void main(String[] args) {
        Start start = new Start();
        if (STARTTYPE == 1) {
            start.interpreter();
        } else if (STARTTYPE == 2) {
            start.codegen();
        }
    }
}
