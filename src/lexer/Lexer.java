package lexer;

import input.Input;

import java.util.HashMap;

/**
 * Lexical analysis
 *
 * @author isHudw dejavudwh
 */

public class Lexer {
    public String text;
    public int length = 0;
    public int lineno = 0;

    private static int READLENGTH = 10;

    private Input input = new Input();
    private String current = "";
    private HashMap<String, Integer> keywordMap = new HashMap<>();
    
    public Lexer() {
        input.newFile();
        initKeywordMap();
    }

    public int lex() {
        String buffer = "";

        while (true) {
            while (current == "") {
                buffer = input.getWholeInput();

                if (buffer.length() == 0) {
                    current = "";
                    return Token.SEMI.ordinal();
                }

                current = buffer;
                current = current.trim();
            }
        }
    }

    private void initKeywordMap() {
        keywordMap.put("auto", Token.CLASS.ordinal());
        keywordMap.put("static", Token.CLASS.ordinal());
        keywordMap.put("register", Token.CLASS.ordinal());
        keywordMap.put("char", Token.TYPE.ordinal());
        keywordMap.put("float", Token.TYPE.ordinal());
        keywordMap.put("double", Token.TYPE.ordinal());
        keywordMap.put("int", Token.TYPE.ordinal());
        keywordMap.put("enum", Token.ENUM.ordinal());
        keywordMap.put("long", Token.TYPE.ordinal());
        keywordMap.put("short", Token.TYPE.ordinal());
        keywordMap.put("void", Token.TYPE.ordinal());
        keywordMap.put("struct", Token.STRUCT.ordinal());
        keywordMap.put("return", Token.RETURN.ordinal());
        keywordMap.put("if", Token.IF.ordinal());
        keywordMap.put("else", Token.ELSE.ordinal());
        keywordMap.put("switch", Token.SWITCH.ordinal());
        keywordMap.put("case", Token.CASE.ordinal());
        keywordMap.put("default", Token.DEFAULT.ordinal());
        keywordMap.put("break", Token.BREAK.ordinal());
        keywordMap.put("for", Token.FOR.ordinal());
        keywordMap.put("while", Token.WHILE.ordinal());
        keywordMap.put("do", Token.DO.ordinal());
        keywordMap.put("goto", Token.GOTO.ordinal());
    }
}
