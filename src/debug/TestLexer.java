package debug;

import lexer.Lexer;
import lexer.Token;

public class TestLexer {
    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        for (int i = 0; i < 41; i++) {
            lexer.advance();
            int lookahead = lexer.lookAhead;
            System.out.println("Symbol: " + Token.getTokenStr(lookahead));
        }
    }
}
