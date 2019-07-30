package start;

import ast.AstBuilder;
import codegen.BaseGenerate;
import codegen.CodeGen;
import codegen.backend.ProgramGenerator;
import interpreter.Interpreter;
import lexer.Lexer;
import parse.LRStateTableParser;
import parse.StateNodeManager;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dejavudwh isHudw
 */

public class Start {
    public static final int INTERPRETER = 1;
    public static final int CODEGEN = 2;

    public static boolean DEBUG = false;

    public static String FILEPATH = "testInput.c";

    public static int STARTTYPE = 1;

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
        parse();
        ProgramGenerator generator = ProgramGenerator.getInstance();
        CodeGen codegen = CodeGen.getInstance();
        AstBuilder astBuilder = AstBuilder.getInstance();
        generator.generateHeader();
        codegen.generate(astBuilder.getSyntaxTreeRoot());
        generator.finish();
    }

    public void start() {
        if (STARTTYPE == Start.INTERPRETER) {
            interpreter();
        } else if (STARTTYPE == Start.CODEGEN) {
            codegen();
        }
    }

    public void setLaunchParam(int type, String filePath, boolean debug) {
        STARTTYPE = type;
        FILEPATH = filePath;
        DEBUG = debug;
    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        for(int i = 0; i < args.length; i += 2) {
            map.put(args[i], args[i + 1]);
        }
        String modeParam = map.get("-m");
        String debugParam = map.get("-d");
        String filepathParam = map.get("-f");

        Start start = new Start();
        int mode = 1;
        boolean debug = false;
        String filePath = "testInput.c";
        if ("codegen".equals(modeParam) || "c".equals(modeParam)) {
            mode = 2;
        }

        if (debugParam != null) {
            debug = true;
        }

        if (filepathParam != null) {
            filePath = filepathParam;
        }

        start.setLaunchParam(mode, filePath, debug);
        start.start();
    }
}
