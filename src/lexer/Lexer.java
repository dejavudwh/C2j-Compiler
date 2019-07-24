package lexer;

import input.Input;

import java.util.HashMap;

/**
 * Lexical analysis
 *
 * @author dejavudwh isHudw
 */

public class Lexer {
    public String text;
    public int length = 0;
    public int lineno = 0;

    public int lookAhead = Token.UNKNOWN_TOKEN.ordinal();

    private static char BLANK = ' ';
    private static char NEWLINE = '\n';
    private static char NULLCHAR = '\0';

    private Input input = new Input();
    private String current = "";
    private HashMap<String, Integer> keywordMap = new HashMap<>();

    public Lexer() {
        input.newFile();
        initKeywordMap();
    }

    public int lex() {
        while (true) {
            while (current.isEmpty()) {
                boolean flag = false;
                while (true) {
                    char c = (char) input.inputAdvance();
                    if ((c == BLANK && !flag) || c == NEWLINE || c == NULLCHAR) {
                        if (c == NEWLINE) {
                            lineno++;
                        }
                        System.out.println("read: " + (int)c);
                        if (c == BLANK && current.isEmpty()) {
                            continue;
                        }
                        break;
                    } else {
                        if (c == '"') {
                            flag = !flag;
                        }
                        current += c;
                        System.out.println("read: " + (int)c);
                    }
                }

                if (current.length() == 0) {
                    current = "";
                    return Token.SEMI.ordinal();
                }
                current = current.trim();
            }

            if (current.isEmpty()) {
                return Token.SEMI.ordinal();
            }

            boolean inString = false;

            for (int i = 0; i < current.length(); i++) {
                length = 0;
                text = current.substring(i, i + 1);
                switch (current.charAt(i)) {
                    case ';':
                        current = current.substring(1);
                        return Token.SEMI.ordinal();
                    case '+':
                        if (current.charAt(i + 1) == '+') {
                            current = current.substring(2);
                            return Token.INCOP.ordinal();
                        }

                        current = current.substring(1);
                        return Token.PLUS.ordinal();

                    case '-':
                        if (current.charAt(i + 1) == '>') {
                            current = current.substring(2);
                            return Token.STRUCTOP.ordinal();
                        } else if (current.charAt(i + 1) == '-') {
                            current = current.substring(2);
                            return Token.DECOP.ordinal();
                        }

                        current = current.substring(1);
                        return Token.MINUS.ordinal();

                    case '[':
                        current = current.substring(1);
                        return Token.LB.ordinal();
                    case ']':
                        current = current.substring(1);
                        return Token.RB.ordinal();
                    case '*':
                        current = current.substring(1);
                        return Token.STAR.ordinal();
                    case '(':
                        current = current.substring(1);
                        return Token.LP.ordinal();
                    case ')':
                        current = current.substring(1);
                        return Token.RP.ordinal();
                    case ',':
                        current = current.substring(1);
                        return Token.COMMA.ordinal();
                    case '{':
                        current = current.substring(1);
                        return Token.LC.ordinal();
                    case '}':
                        current = current.substring(1);
                        return Token.RC.ordinal();
                    case '=':
                        if (i + 1 < current.length() && current.charAt(i + 1) == '=') {
                            current = current.substring(2);
                            return Token.RELOP.ordinal();
                        }

                        current = current.substring(1);
                        return Token.EQUAL.ordinal();

                    case '?':
                        current = current.substring(1);
                        return Token.QUEST.ordinal();
                    case ':':
                        current = current.substring(1);
                        return Token.COLON.ordinal();
                    case '&':
                        current = current.substring(1);
                        return Token.AND.ordinal();
                    case '|':
                        current = current.substring(1);
                        return Token.OR.ordinal();
                    case '^':
                        current = current.substring(1);
                        return Token.XOR.ordinal();

                    case '/':
                    case '%':
                        current = current.substring(1);
                        return Token.DIVOP.ordinal();
                    case '>':
                    case '<':
                        boolean isBitOpr = (i + 1 < current.length() && current.charAt(i) == '<' && current.charAt(i + 1) == '<')
                                || (i + 1 < current.length() && current.charAt(i) == '>' && current.charAt(i + 1) == '>');
                        if (i + 1 < current.length() && current.charAt(i + 1) == '=') {
                            text = current.substring(0, 2);
                            current = current.substring(2);
                        } else if (isBitOpr) {
                            text = current.substring(0, 2);
                            current = current.substring(2);
                            return Token.SHIFTOP.ordinal();
                        } else {
                            current = current.substring(1);
                        }

                        return Token.RELOP.ordinal();

                    case '\n':
                    case '\t':
                    case ' ':
                        current = current.substring(1);
                        return Token.WHITE_SPACE.ordinal();

                    case '.':
                        current = current.substring(1);
                        return Token.STRUCTOP.ordinal();

                    case '"':
                        inString = true;
                        i++;
                        int begin = i;
                        while (i < current.length()) {
                            if (current.charAt(i) != '"') {
                                length++;
                            } else {
                                break;
                            }

                            i++;
                        }

                        if (i >= current.length() && inString) {
                            System.err.println("Missing the ending quotation mark!");
                            System.exit(1);
                        }

                        text = current.substring(begin, length + 1);
                        current = current.substring(length + 2);

                        return Token.STRING.ordinal();

                    default:
                        if (!isAlorNum(current.charAt(i))) {
                            return Token.UNKNOWN_TOKEN.ordinal();
                        } else {

                            while (i < current.length() && isAlorNum(current.charAt(i))) {
                                i++;
                                length++;
                            }

                            text = current.substring(0, length);
                            current = current.substring(length);
                            return translateStringToToken();
                        }
                }
            }
        }
    }

    public boolean match(int token) {
        if (lookAhead == -1) {
            lookAhead = lex();
        }

        return token == lookAhead;
    }

    public void advance() {
        lookAhead = lex();
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

    private int translateStringToToken() {
        int type = keywordOrNumber();
        if (type != Token.UNKNOWN_TOKEN.ordinal()) {
            return type;
        }

        return Token.NAME.ordinal();
    }

    private int keywordOrNumber() {
        int type = Token.UNKNOWN_TOKEN.ordinal();
        if (Character.isAlphabetic(text.charAt(0))) {
            type = isKeyWord(text);
        } else {
            if (isNum()) {
                type = Token.NUMBER.ordinal();
            }
        }

        return type;
    }

    private boolean isNum() {
        int pos = 0;
        if (text.charAt(0) == '-' || text.charAt(0) == '+') {
            pos++;
        }

        for (; pos < text.length(); pos++) {
            if (!Character.isDigit(text.charAt(pos))) {
                break;
            }
        }

        return pos == text.length();
    }

    private int isKeyWord(String str) {
        if (keywordMap.containsKey(str)) {
            return keywordMap.get(str);
        }

        return Token.UNKNOWN_TOKEN.ordinal();
    }

    private boolean isAlorNum(char c) {
        return (Character.isAlphabetic(c) || Character.isDigit(c));
    }

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        for (int i = 0; i < 3; i++) {
            lexer.advance();
            int token = lexer.lookAhead;
            System.out.println(Token.getTokenStr(token));
        }
    }

}
