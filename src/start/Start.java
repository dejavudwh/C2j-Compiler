package start;

import ast.AstBuilder;
import codegen.BaseExecutor;
import codegen.CodeGen;
import codegen.generator.ProgramGenerator;
import interpreter.Interpreter;
import lexer.Lexer;
import parse.LRStateTableParser;
import parse.StateNodeManager;

/**
 *
 * @author dejavudwh isHudw
 */

public class Start {
    public static final int INTERPRETER = 1;
    public static final int CODEGEN = 2;

    public static int STARTTYPE = 2;

    public void parse() {
        StateNodeManager snm = StateNodeManager.getInstance();
        snm.buildTransitionStateMachine();
        LRStateTableParser tableParser = new LRStateTableParser(new Lexer());
        tableParser.parse();
    }

    public void interpreter() {
        parse();
        Interpreter interpreter = Interpreter.getInstance();
        AstBuilder astBuilder = AstBuilder.getInstance();
        interpreter.execute(astBuilder.getSyntaxTreeRoot());
    }

    public void codegen() {
        BaseExecutor.isCompileMode = true;
        parse();
        ProgramGenerator generator = ProgramGenerator.getInstance();
        CodeGen codegen = CodeGen.getInstance();
        AstBuilder astBuilder = AstBuilder.getInstance();
        generator.generateHeader();
        codegen.Execute(astBuilder.getSyntaxTreeRoot());
        generator.finish();
    }

    public static void main(String[] args) {
        Start start = new Start();
        if (STARTTYPE == Start.INTERPRETER) {
            start.interpreter();
        } else if (STARTTYPE == Start.CODEGEN) {
            start.codegen();
        }
    }
}
