package lexer;

import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sun.org.apache.bcel.internal.generic.SWITCH;

/**
 * C token enumeration
 *
 * @author isHudw dejavudwh
 */

public enum Token {
    //non-terminal
    PROGRAM, EXT_DEF_LIST, EXT_DEF, OPT_SPECIFIERS, EXT_DECL_LIST,
    EXT_DECL, VAR_DECL, SPECIFIERS, TYPE_OR_CLASS, TYPE_NT,

    //struct
    STRUCT_SPECIFIER, OPT_TAG, DEF_LIST, TAG, DEF, DECL_LIST, DECL,

    //function
    FUNCT_DECL, VAR_LIST, PARAM_DECLARATION,

    //enum
    ENUM_SPECIFIER, OPT_ENUM_LIST, ENUMERATOR_LIST, ENUMERATOR, ENUM_NT,

    CONST_EXPR, ARGS,

    COMPOUND_STMT, LOCAL_DEFS, STMT_LIST, INITIALIZER, EXPR, NO_COMMA_EXPR, BINARY, UNARY, STATEMENT,

    TEST, OPT_EXPR, END_OPT_EXPR, TARGET, ELSE_STATEMENT, IF_STATEMENT, IF_ELSE_STATEMENT,

    TYPE_SPECIFIER, NEW_NAME, NAME_NT,

    //terminals
    NAME, TYPE, STRUCT, CLASS, LP, RP, LB, RB, PLUS, LC, RC, NUMBER, STRING, QUEST, COLON,
    RELOP, ANDAND, OR, AND, EQUOP, SHIFTOP, DIVOP, XOR, MINUS, INCOP, DECOP, STRUCTOP,
    RETURN, IF, ELSE, SWITCH, CASE, DEFAULT, BREAK, WHILE, FOR, DO, CONTINUE, GOTO,

    //enum def
    ENUM,

    COMMA, SEMI, WHITE_SPACE, EQUAL, TTYPE, STAR, UNKNOWN_TOKEN;

    public static final int FIRST_TERMINAL_INDEX = NAME.ordinal();
    public static final int LAST_TERMINAL_INDEX = UNKNOWN_TOKEN.ordinal();

    public static final int FIRST_NON_TERMINAL_INDEX = PROGRAM.ordinal();
    public static final int LAST_NON_TERMINAL_INDEX = NAME_NT.ordinal();

    public static String getTokenStr(int val) {
        return Token.values()[val].toString();
    }

    public static boolean isTerminal(int val) {
        if (FIRST_TERMINAL_INDEX <= val && val <= LAST_TERMINAL_INDEX) {
            return true;
        }

        return false;
    }
}
