package parse;

import lexer.Token;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Initialization of syntactic derivation
 *
 * @author dejavudwh isHudw
 */

public class SyntaxProductionInit {
    public static final int TYPE_TO_TYPE_SPECIFIER = 11;
    public static final int SPECIFIERS_TypeOrClass_TO_SPECIFIERS = 9;
    public static final int NAME_TO_NewName = 12;
    public static final int START_VarDecl_TO_VarDecl = 14;
    public static final int ExtDeclList_COMMA_ExtDecl_TO_ExtDeclList = 5;
    public static final int OptSpecifier_ExtDeclList_Semi_TO_ExtDef = 2;
    public static final int TypeNT_VarDecl_TO_ParamDeclaration = 20;
    public static final int VarList_COMMA_ParamDeclaration_TO_VarList = 19;

    public static final int NewName_LP_VarList_RP_TO_FunctDecl = 16;
    public static final int NewName_LP_RP_TO_FunctDecl = 17;

    public static final int Start_VarDecl_TO_VarDecl = 39;
    public static final int DeclList_Comma_Decl_TO_DeclList = 33;
    public static final int Specifiers_DeclList_Semi_TO_Def = 30;
    public static final int DefList_Def_TO_DefList = 29;
    public static final int Name_To_Tag = 27;
    public static final int Struct_OptTag_LC_DefList_RC_TO_StructSpecifier = 24;
    public static final int StructSpecifier_TO_TypeSpecifier = 23;

    public static final int Enum_TO_EnumNT = 41;
    public static final int NameNT_TO_Emurator = 44;
    public static final int Name_TO_NameNT = 45;
    public static final int Name_Eequal_ConstExpr_TO_Enuerator = 46;
    public static final int Number_TO_ConstExpr = 47;
    public static final int EnumSpecifier_TO_TypeSpecifier = 49;

    public static final int NoCommaExpr_TO_Expr = 53;
    public static final int NoCommaExpr_Equal_NoCommaExpr_TO_NoCommaExpr = 54;
    public static final int Binary_TO_NoCommaExpr = 56;
    public static final int Uanry_TO_Binary = 57;
    public static final int Number_TO_Unary = 58;
    public static final int Name_TO_Unary = 59;
    public static final int String_TO_Unary = 60;
    //STATEMENT -> EXPR SEMI(63)
    public static final int Expr_Semi_TO_Statement = 63;
    //STMT_LIST ->  STATEMENT(62)
    public static final int Statement_TO_StmtList = 62;
    //STMT_LIST -> STMT_LIST STATEMENT(61)
    public static final int StmtList_Statement_TO_StmtList = 61;
    public static final int Binary_Plus_Binary_TO_Binary = 94;
    //STATEMENT -> LOCAL_DEFS(68)
    public static final int LocalDefs_TO_Statement = 68;
    //VAR_DECL -> VAR_DECL LB CONST_EXPR RB  a[5] (109)
    public static final int VarDecl_LB_ConstExpr_RB_TO_VarDecl = 109;
    //UNARY -> UNARY LB EXPR RB (110)
    public static final int Unary_LB_Expr_RB_TO_Unary = 101;
    //TEST -> EXPR 76
    public static final int Expr_TO_Test = 76;
    //IF_STATEMENT -> IF LP TEST RP STATEMENT (72)
    public static final int If_Test_Statement_TO_IFStatement = 72;
    //BINARY -> BINARY RELOP BINARY (65)
    public static final int Binary_RelOP_Binary_TO_Binray = 65;
    //IF_ELSE_STATEMENT ->IF_ELSE_STATEMENT ELSE STATEMENT 74
    public static final int IfElseStatemnt_Else_Statemenet_TO_IfElseStatement = 74;
    //OPT_EXPR -> EXPR SEMI(85)
    public static final int Expr_Semi_TO_OptExpr = 85;
    //OPT_EXPR -> SEMI(86)
    public static final int Semi_TO_OptExpr = 86;
    //END_OPT_EXPR -> EXPR(87)
    public static final int Expr_TO_EndOpt = 87;
    //STATEMENT -> FOR LP OPT_EXPR  TEST SEMI END_OPT_EXPR RP STATEMENT(84)
    public static final int FOR_OptExpr_Test_EndOptExpr_Statement_TO_Statement = 84;
    //STATEMENT -> WHILE LP TEST RP STATEMENT (83)
    public static final int While_LP_Test_Rp_TO_Statement = 83;
    //STATEMENT -> DO STATEMENT WHILE LP TEST RP SEMI(88)
    public static final int Do_Statement_While_Test_To_Statement = 88;
    //UNARY -> UNARY INCOP i++ (96)
    public static final int Unary_Incop_TO_Unary = 96;
    //UNARY -> INCOP UNARY ++i (97)
    public static final int Incop_Unary_TO_Unary = 97;
    //UNARY -> MINUS UNARY
    public static final int Minus_Unary_TO_Unary = 98;
    //STATEMENT -> COMPOUND_STMT (71)
    public static final int CompountStmt_TO_Statement = 71;
    //DECL -> VAR_DECL EQUAL INITIALIZER 77
    public static final int VarDecl_Equal_Initializer_TO_Decl = 77;
    //INITIALIZER -> EXPR 78
    public static final int Expr_TO_Initializer = 78;
    //LOCAL_DEFS -> DEF_LIST(52)
    public static final int DefList_TO_LocalDefs = 52;
    //VAR_DECL ->  NEW_NAME(13)
    public static final int NewName_TO_VarDecl = 13;
    //COMPOUND_STMT-> LC LOCAL_DEFS STMT_LIST RC(51)
    public static final int LocalDefs_StmtList_TO_CompoundStmt = 51;
    //EXT_DEF -> OPT_SPECIFIERS FUNCT_DECL COMPOUND_STMT(50)
    public static final int OptSpecifiers_FunctDecl_CompoundStmt_TO_ExtDef = 50;
    //UNARY -> UNARY LP RP  fun()
    public static final int Unary_LP_RP_TO_Unary = 103;
    //UNARY -> UNARY LP ARGS RP fun(a, b ,c)
    public static final int Unary_LP_ARGS_RP_TO_Unary = 102;
    //ARGS -> NO_COMMA_EXPR  (104)
    public static final int NoCommaExpr_TO_Args = 104;
    //ARGS -> NO_COMMA_EXPR COMMA ARGS (105)
    public static final int NoCommaExpr_Comma_Args_TO_Args = 105;
    //STATEMENT -> RETURN SEMI (111)
    public static final int Return_Semi_TO_Statement = 111;
    //STATEMENT -> RETURN EXPR SEMI (64)
    public static final int Return_Expr_Semi_TO_Statement = 64;
    //UNARY -> LP EXPR RP (112)
    public static final int LP_Expr_RP_TO_Unary = 112;
    //UNARY -> UNARY DECOP i--
    public static final int Unary_DecOp_TO_Unary = 113;
    //BINARY -> BINARY DIVOP BINARY(90)
    public static final int Binary_DivOp_Binary_TO_Binary = 90;
    //BINARY -> BINARY MINUS BINARY(95)
    public static final int Binary_Minus_Binary_TO_Binary = 95;
    //UNARY -> STAR UNARY 99
    public static final int Start_Unary_TO_Unary = 99;
    //UNARY -> UNARY STRUCTOP NAME 100
    public static final int Unary_StructOP_Name_TO_Unary = 100;
    //DEF_LIST ->  DEF (28) for codegen
    public static final int Def_To_DefList = 28;
    public static final int Binary_Start_Binary_TO_Binary = 67;

    private static SyntaxProductionInit instance = null;
    private int productionNum = 0;
    private HashMap<Integer, ArrayList<Production>> productionMap = new HashMap<>();
    private HashMap<Integer, Symbols> symbolMap = new HashMap<>();
    private ArrayList<Symbols> symbolArray = new ArrayList<>();

    private SyntaxProductionInit() {
        initVariableDeclaration();
        initFunctionProductions();
        initStructureProductions();
        initEnumProductions();
        initFunctionDefinition();
        initFunctionDefinition2();
        initFunctionDefinitionWithIfElse();
        initFunctionDefinitionWithSwitchCase();
        initFunctionDefinitionWithLoop();
        initComputingOperation();
        initRemaindingProduction();

        addTerminalToSymbolMapAndArray();
    }

    public static SyntaxProductionInit getInstance() {
        if (instance == null) {
            instance = new SyntaxProductionInit();
        }

        return instance;
    }

    private void initVariableDeclaration() {
        productionMap.clear();
        /*LB: { RB:}
         *
         * C variable declaration grammar
         *  PROGRAM -> EXT_DEF_LIST
         *
         *  EXT_DEF_LIST -> EXT_DEF_LIST EXT_DEF
         *
         *  EXT_DEF -> OPT_SPECIFIERS EXT_DECL_LIST  SEMI
         *             | OPT_SPECIFIERS SEMI
         *
         *
         *  EXT_DECL_LIST ->   EXT_DECL
         *                   | EXT_DECL_LIST COMMA EXT_DECL
         *
         *  EXT_DECL -> VAR_DECL
         *
         *  OPT_SPECIFIERS -> CLASS TTYPE
         *                   | TTYPE
         *                   | SPECIFIERS
         *                   | EMPTY?
         *
         *  SPECIFIERS -> TYPE_OR_CLASS
         *                | SPECIFIERS TYPE_OR_CLASS
         *
         *
         *  TYPE_OR_CLASS -> TYPE_SPECIFIER
         *                   | CLASS
         *
         *  TYPE_SPECIFIER ->  TYPE
         *
         *  NEW_NAME -> NAME
         *
         *  NAME_NT -> NAME
         *
         *  VAR_DECL -> | NEW_NAME
         *
         *              | START VAR_DECL
         *
         */

        //PROGRAM -> EXT_DEF_LIST
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{Token.EXT_DEF_LIST.ordinal()});
        Production production = new Production(productionNum, Token.PROGRAM.ordinal(), 0, right);
        productionNum++;
        addProduction(production, true);


        //EXT_DEF_LIST -> EXT_DEF_LIST EXT_DEF
        right = getProductionRight(new int[]{Token.EXT_DEF_LIST.ordinal(), Token.EXT_DEF.ordinal()});
        production = new Production(productionNum, Token.EXT_DEF_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, true);


        //EXT_DEF -> OPT_SPECIFIERS EXT_DECL_LIST  SEMI
        right = getProductionRight(new int[]{Token.OPT_SPECIFIERS.ordinal(), Token.EXT_DECL_LIST.ordinal(), Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.EXT_DEF.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //EXT_DEF -> OPT_SPECIFIERS  SEMI
        right = getProductionRight(new int[]{Token.OPT_SPECIFIERS.ordinal(), Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.EXT_DEF.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //EXT_DECL_LIST ->   EXT_DECL
        right = getProductionRight(new int[]{Token.EXT_DECL.ordinal()});
        production = new Production(productionNum, Token.EXT_DECL_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        ///EXT_DECL_LIST ->EXT_DECL_LIST COMMA EXT_DECL
        right = getProductionRight(new int[]{Token.EXT_DECL_LIST.ordinal(), Token.COMMA.ordinal(), Token.EXT_DECL.ordinal()});
        production = new Production(productionNum, Token.EXT_DECL_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //EXT_DECL -> VAR_DECL
        right = getProductionRight(new int[]{Token.VAR_DECL.ordinal()});
        production = new Production(productionNum, Token.EXT_DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //OPT_SPECIFIERS -> SPECIFIERS
        right = getProductionRight(new int[]{Token.SPECIFIERS.ordinal()});
        production = new Production(productionNum, Token.OPT_SPECIFIERS.ordinal(), 0, right);
        productionNum++;
        addProduction(production, true);

        //SPECIFIERS -> TYPE_OR_CLASS
        right = getProductionRight(new int[]{Token.TYPE_OR_CLASS.ordinal()});
        production = new Production(productionNum, Token.SPECIFIERS.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //SPECIFIERS -> SPECIFIERS TYPE_OR_CLASS
        right = getProductionRight(new int[]{Token.SPECIFIERS.ordinal(), Token.TYPE_OR_CLASS.ordinal()});
        production = new Production(productionNum, Token.SPECIFIERS.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //TYPE_OR_CLASS -> TYPE_SPECIFIER
        right = getProductionRight(new int[]{Token.TYPE_SPECIFIER.ordinal()});
        production = new Production(productionNum, Token.TYPE_OR_CLASS.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //TYPE_SPECIFIER ->  TYPE
        right = getProductionRight(new int[]{Token.TYPE.ordinal()});
        production = new Production(productionNum, Token.TYPE_SPECIFIER.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //NEW_NAME -> NAME
        right = getProductionRight(new int[]{Token.NAME.ordinal()});
        production = new Production(productionNum, Token.NEW_NAME.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //VAR_DECL ->  NEW_NAME(13)
        right = getProductionRight(new int[]{Token.NEW_NAME.ordinal()});
        production = new Production(productionNum, Token.VAR_DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        ///VAR_DECL ->START VAR_DECL
        right = getProductionRight(new int[]{Token.STAR.ordinal(), Token.VAR_DECL.ordinal()});
        production = new Production(productionNum, Token.VAR_DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


    }

    private void initFunctionProductions() {
        /*production num begin with 15
         *
         * EXT_DEF -> OPT_SPECIFIERS FUNCT_DECL SEMI
         *
         * FUNCT_DECL -> NEW_NAME LP VAR_LIST RP
         *              | NEW_NAME LP RP
         * VAR_LIST ->  PARAM_DECLARATION
         *              | VAR_LIST COMMA PARAM_DECLARATION
         * PARAM_DECLARATION -> TYPE_NT VAR_DECL
         *
         * TYPE_NT -> TYPE_SPECIFIER
         *            | TYPE TYPE_SPECIFIER
         */

        //EXT_DEF -> OPT_SPECIFIERS FUNCT_DECL SEMI(15)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{Token.OPT_SPECIFIERS.ordinal(), Token.FUNCT_DECL.ordinal(),
                Token.SEMI.ordinal()});
        Production production = new Production(productionNum, Token.EXT_DEF.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //FUNCT_DECL -> NEW_NAME LP VAR_LIST RP(16)
        right = null;
        right = getProductionRight(new int[]{Token.NEW_NAME.ordinal(), Token.LP.ordinal(),
                Token.VAR_LIST.ordinal(), Token.RP.ordinal()});
        production = new Production(productionNum, Token.FUNCT_DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //FUNCT_DECL ->  NEW_NAME LP RP(17)
        right = null;
        right = getProductionRight(new int[]{Token.NEW_NAME.ordinal(), Token.LP.ordinal(), Token.RP.ordinal()});
        production = new Production(productionNum, Token.FUNCT_DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //VAR_LIST ->  PARAM_DECLARATION
        right = null;
        right = getProductionRight(new int[]{Token.PARAM_DECLARATION.ordinal()});
        production = new Production(productionNum, Token.VAR_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //VAR_LIST -> VAR_LIST COMMA PARAM_DECLARATION
        right = null;
        right = getProductionRight(new int[]{Token.VAR_LIST.ordinal(), Token.COMMA.ordinal(),
                Token.PARAM_DECLARATION.ordinal()});
        production = new Production(productionNum, Token.VAR_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //PARAM_DECLARATION -> TYPE_NT VAR_DECL
        right = null;
        right = getProductionRight(new int[]{Token.TYPE_NT.ordinal(), Token.VAR_DECL.ordinal()});
        production = new Production(productionNum, Token.PARAM_DECLARATION.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //TYPE_NT -> TYPE_SPECIFIER
        right = null;
        right = getProductionRight(new int[]{Token.TYPE_SPECIFIER.ordinal()});
        production = new Production(productionNum, Token.TYPE_NT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //TYPE_NT -> TYPE TYPE_SPECIFIER
        right = null;
        right = getProductionRight(new int[]{Token.TYPE.ordinal(), Token.TYPE_SPECIFIER.ordinal()});
        production = new Production(productionNum, Token.TYPE_NT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

    }

    private void initStructureProductions() {
        /* production number begin from 23
         *
         * TYPE_SPECIFIER -> STRUCT_SPECIFIER
         * STTUCT_SPECIFIER -> STRUCT OPT_TAG LC DEF_LIST RC
         *                     | STRUCT TAG
         * OPT_TAG -> TAG
         *
         * TAG -> NAME
         *
         *
         * DEF_LIST ->  DEF_LIST DEF
         *
         *
         *
         *
         *
         * DEF -> SPECIFIERS  DECL_LIST SEMI
         *        | SPECIFIERS SEMI
         *
         *
         * DECL_LIST -> DECL
         *             | DECL_LIST COMMA DECL
         *
         * DECL -> VAR_DECL
         *
         * VAR_DECL -> NEW_NAME
         *             | VAR_DECL LP RP
         *             | VAR_DECL LP VAR_LIST RP
         *             | LP VAR_DECL RP
         *             | START VAR_DECL
         */

        //TYPE_SPECIFIER -> STRUCT_SPECIFIER  (23)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{Token.STRUCT_SPECIFIER.ordinal()});
        Production production = new Production(productionNum, Token.TYPE_SPECIFIER.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STTUCT_SPECIFIER -> STRUCT OPT_TAG LC DEF_LIST RC (24)
        right = getProductionRight(new int[]{Token.STRUCT.ordinal(), Token.OPT_TAG.ordinal(),
                Token.LC.ordinal(), Token.DEF_LIST.ordinal(), Token.RC.ordinal()});
        production = new Production(productionNum, Token.STRUCT_SPECIFIER.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STRUCT_SPECIFIER -> STRUCT TAG (25)
        right = getProductionRight(new int[]{Token.STRUCT.ordinal(), Token.TAG.ordinal()});
        production = new Production(productionNum, Token.STRUCT_SPECIFIER.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //OPT_TAG -> TAG (26)
        right = getProductionRight(new int[]{Token.TAG.ordinal()});
        production = new Production(productionNum, Token.OPT_TAG.ordinal(), 0, right);
        productionNum++;
        addProduction(production, true);

        //TAG -> NAME (27)
        right = getProductionRight(new int[]{Token.NAME.ordinal()});
        production = new Production(productionNum, Token.TAG.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //DEF_LIST ->  DEF (28)
        right = getProductionRight(new int[]{Token.DEF.ordinal()});
        production = new Production(productionNum, Token.DEF_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //DEF_LIST -> DEF_LIST DEF (29)
        right = getProductionRight(new int[]{Token.DEF_LIST.ordinal(), Token.DEF.ordinal()});
        production = new Production(productionNum, Token.DEF_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //DEF -> SPECIFIERS DECL_LIST SEMI (30)
        right = getProductionRight(new int[]{Token.SPECIFIERS.ordinal(), Token.DECL_LIST.ordinal(),
                Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.DEF.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //DEF -> SPECIFIERS SEMI (31)
        right = getProductionRight(new int[]{Token.SPECIFIERS.ordinal(), Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.DEF.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //DECL_LIST -> DECL (32)
        right = getProductionRight(new int[]{Token.DECL.ordinal()});
        production = new Production(productionNum, Token.DECL_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //DECL_LIST -> DECL_LIST COMMA DECL (33)
        right = getProductionRight(new int[]{Token.DECL_LIST.ordinal(), Token.COMMA.ordinal(),
                Token.DECL.ordinal()});
        production = new Production(productionNum, Token.DECL_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //DECL -> VAR_DECL (34)
        right = getProductionRight(new int[]{Token.VAR_DECL.ordinal()});
        production = new Production(productionNum, Token.DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //VAR_DECL -> NEW_NAME (35)
        right = getProductionRight(new int[]{Token.NEW_NAME.ordinal()});
        production = new Production(productionNum, Token.VAR_DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //VAR_DECL -> VAR_DECL LP RP (36)
        right = getProductionRight(new int[]{Token.VAR_DECL.ordinal(), Token.LP.ordinal(),
                Token.RP.ordinal()});
        production = new Production(productionNum, Token.VAR_DECL.ordinal(), 0, right);
        productionNum++;
        // addProduction(production, false);

        //VAR_DECL -> VAR_DECL LP VAR_LIS RP (37)
        right = getProductionRight(new int[]{Token.VAR_DECL.ordinal(), Token.LP.ordinal(),
                Token.VAR_LIST.ordinal(), Token.RP.ordinal()});
        production = new Production(productionNum, Token.VAR_DECL.ordinal(), 0, right);
        productionNum++;
        // addProduction(production, false);

        //VAR_DECL -> LP VAR_DECL RP (38)
        right = getProductionRight(new int[]{Token.LP.ordinal(), Token.VAR_DECL.ordinal(),
                Token.RP.ordinal()});
        production = new Production(productionNum, Token.VAR_DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //VAR_DECL -> STAR VAR_DECL (39)
        right = getProductionRight(new int[]{Token.STAR.ordinal(), Token.VAR_DECL.ordinal()});
        production = new Production(productionNum, Token.VAR_DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);
    }

    private void initEnumProductions() {
        /*
         * begin from production number 40
         *
         */
        //ENUM_SPECIFIER -> ENUM_NT NAME_NT OPT_ENUM_LIST(40)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{Token.ENUM_NT.ordinal(), Token.NAME_NT.ordinal(), Token.OPT_ENUM_LIST.ordinal()});
        Production production = new Production(productionNum, Token.ENUM_SPECIFIER.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //ENUM_NT -> ENUM(41)
        right = getProductionRight(new int[]{Token.ENUM.ordinal()});
        production = new Production(productionNum, Token.ENUM_NT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //ENUMERATOR_LIST -> ENUMERATOR(42)
        right = getProductionRight(new int[]{Token.ENUMERATOR.ordinal()});
        production = new Production(productionNum, Token.ENUMERATOR_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //EMERATOR_LIST -> ENUMERATOR_LIST COMMA ENUMERATOR(43)
        right = getProductionRight(new int[]{Token.ENUMERATOR_LIST.ordinal(), Token.COMMA.ordinal(),
                Token.ENUMERATOR.ordinal()});
        production = new Production(productionNum, Token.ENUMERATOR_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //ENUMERATOR -> NAME_NT(44)
        right = getProductionRight(new int[]{Token.NAME_NT.ordinal()});
        production = new Production(productionNum, Token.ENUMERATOR.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //NAME_NT -> NAME(45)
        right = getProductionRight(new int[]{Token.NAME.ordinal()});
        production = new Production(productionNum, Token.NAME_NT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //ENUMERATOR -> NAME_NT EQUAL CONST_EXPR(46)
        right = getProductionRight(new int[]{Token.NAME_NT.ordinal(), Token.EQUAL.ordinal(),
                Token.CONST_EXPR.ordinal()});
        production = new Production(productionNum, Token.ENUMERATOR.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //CONST_EXPR -> NUMBER (47)
        right = getProductionRight(new int[]{Token.NUMBER.ordinal()});
        production = new Production(productionNum, Token.CONST_EXPR.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //OPT_ENUM_LIST -> LC ENUMERATOR_LIST RC (48)
        right = getProductionRight(new int[]{Token.LC.ordinal(), Token.ENUMERATOR_LIST.ordinal(), Token.RC.ordinal()});
        production = new Production(productionNum, Token.OPT_ENUM_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, true);

        //TYPE_SPECIFIER -> ENUM_SPECIFIER (49)
        right = getProductionRight(new int[]{Token.ENUM_SPECIFIER.ordinal()});
        production = new Production(productionNum, Token.TYPE_SPECIFIER.ordinal(), 0, right);
        productionNum++;
        addProduction(production, true);


    }

    private void initFunctionDefinition() {
        /*
         * begin production number 50
         */
        //EXT_DEF -> OPT_SPECIFIERS FUNCT_DECL COMPOUND_STMT(50)

        ArrayList<Integer> right = null;
        Production production = null;
        right = getProductionRight(new int[]{Token.OPT_SPECIFIERS.ordinal(), Token.FUNCT_DECL.ordinal(),
                Token.COMPOUND_STMT.ordinal()});
        production = new Production(productionNum, Token.EXT_DEF.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //COMPOUND_STMT-> LC LOCAL_DEFS STMT_LIST RC(51)
        right = getProductionRight(new int[]{Token.LC.ordinal(), Token.LOCAL_DEFS.ordinal(),
                Token.STMT_LIST.ordinal(), Token.RC.ordinal()});
        production = new Production(productionNum, Token.COMPOUND_STMT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //LOCAL_DEFS -> DEF_LIST(52)
        right = getProductionRight(new int[]{Token.DEF_LIST.ordinal()});
        production = new Production(productionNum, Token.LOCAL_DEFS.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //EXPR -> NO_COMMA_EXPR(53)
        right = getProductionRight(new int[]{Token.NO_COMMA_EXPR.ordinal()});
        production = new Production(productionNum, Token.EXPR.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //NO_COMMA_EXPR -> NO_COMMA_EXPR EQUAL NO_COMMA_EXPR(54)
        right = getProductionRight(new int[]{Token.NO_COMMA_EXPR.ordinal(), Token.EQUAL.ordinal(), Token.NO_COMMA_EXPR.ordinal()});
        production = new Production(productionNum, Token.NO_COMMA_EXPR.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //NO_COMMA_EXPR -> NO_COMMA_EXPR QUEST  NO_COMMA_EXPR COLON NO_COMMA_EXPR(55)
        right = getProductionRight(new int[]{Token.NO_COMMA_EXPR.ordinal(), Token.QUEST.ordinal(), Token.NO_COMMA_EXPR.ordinal(),
                Token.COLON.ordinal(), Token.NO_COMMA_EXPR.ordinal()});
        production = new Production(productionNum, Token.NO_COMMA_EXPR.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //NO_COMMA_EXPR -> BINARY(56)
        right = getProductionRight(new int[]{Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.NO_COMMA_EXPR.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //BINARY -> UNARY (57)
        right = getProductionRight(new int[]{Token.UNARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> NUMBER (58)
        right = getProductionRight(new int[]{Token.NUMBER.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> NAME (59)
        right = getProductionRight(new int[]{Token.NAME.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> STRING(60)
        right = getProductionRight(new int[]{Token.STRING.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STMT_LIST -> STMT_LIST STATEMENT(61)
        right = getProductionRight(new int[]{Token.STMT_LIST.ordinal(), Token.STATEMENT.ordinal()});
        production = new Production(productionNum, Token.STMT_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, true);

        //STMT_LIST ->  STATEMENT(62)
        right = getProductionRight(new int[]{Token.STATEMENT.ordinal()});
        production = new Production(productionNum, Token.STMT_LIST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, true);

        //STATEMENT -> EXPR SEMI(63)
        right = getProductionRight(new int[]{Token.EXPR.ordinal(), Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STATEMENT -> RETURN EXPR SEMI (64)
        right = getProductionRight(new int[]{Token.RETURN.ordinal(), Token.EXPR.ordinal(), Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //BINARY -> BINARY RELOP BINARY (65)
        right = getProductionRight(new int[]{Token.BINARY.ordinal(), Token.RELOP.ordinal(), Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //BINARY -> BINARY EQUOP BINARY (66)
        right = getProductionRight(new int[]{Token.BINARY.ordinal(), Token.EQUOP.ordinal(), Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //BINARY -> BINARY START BINARY (67)
        right = getProductionRight(new int[]{Token.BINARY.ordinal(), Token.STAR.ordinal(), Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STATEMENT -> LOCAL_DEFS(68)
        right = getProductionRight(new int[]{Token.LOCAL_DEFS.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

    }

    private void initFunctionDefinition2() {
        //COMPOUND_STMT -> LC RC(69)
        ArrayList<Integer> right = null;
        Production production = null;
        right = getProductionRight(new int[]{Token.LC.ordinal(), Token.RC.ordinal()});
        production = new Production(productionNum, Token.COMPOUND_STMT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //COMPOUNT_STMT -> LC STMT_LIST RC(70)
        right = getProductionRight(new int[]{Token.LC.ordinal(), Token.STMT_LIST.ordinal(), Token.RC.ordinal()});
        production = new Production(productionNum, Token.COMPOUND_STMT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

    }

    private void initFunctionDefinitionWithIfElse() {
        //STATEMENT -> COMPOUND_STMT (71)
        ArrayList<Integer> right = null;
        Production production = null;
        right = getProductionRight(new int[]{Token.COMPOUND_STMT.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //IF_STATEMENT -> IF LP TEST RP STATEMENT (72)
        right = getProductionRight(new int[]{Token.IF.ordinal(), Token.LP.ordinal(),
                Token.TEST.ordinal(), Token.RP.ordinal(), Token.STATEMENT.ordinal()});
        production = new Production(productionNum, Token.IF_STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //IF_ELSE_STATEMENT -> IF_STATEMENT 73
        right = getProductionRight(new int[]{Token.IF_STATEMENT.ordinal()});
        production = new Production(productionNum, Token.IF_ELSE_STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //IF_ELSE_STATEMENT ->IF_ELSE_STATEMENT ELSE STATEMENT 74
        right = getProductionRight(new int[]{Token.IF_ELSE_STATEMENT.ordinal(), Token.ELSE.ordinal(),
                Token.STATEMENT.ordinal()});
        production = new Production(productionNum, Token.IF_ELSE_STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STATEMENT -> IF_ELSE_STATEMENT 75
        right = getProductionRight(new int[]{Token.IF_ELSE_STATEMENT.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


        //TEST -> EXPR 76
        right = getProductionRight(new int[]{Token.EXPR.ordinal()});
        production = new Production(productionNum, Token.TEST.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //DECL -> VAR_DECL EQUAL INITIALIZER 77
        right = getProductionRight(new int[]{Token.VAR_DECL.ordinal(), Token.EQUAL.ordinal(),
                Token.INITIALIZER.ordinal()});
        production = new Production(productionNum, Token.DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //INITIALIZER -> EXPR 78
        right = getProductionRight(new int[]{Token.EXPR.ordinal()});
        production = new Production(productionNum, Token.INITIALIZER.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);
    }

    private void initFunctionDefinitionWithSwitchCase() {
        //STATEMENT -> SWITCH LP EXPR RP COMPOUND_STATEMENT (79)
        ArrayList<Integer> right = null;
        Production production = null;
        right = getProductionRight(new int[]{Token.SWITCH.ordinal(), Token.LP.ordinal(), Token.EXPR.ordinal(),
                Token.RP.ordinal(), Token.STATEMENT.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STATEMENT -> CASE CONST_EXPR COLON(80)
        right = getProductionRight(new int[]{Token.CASE.ordinal(), Token.CONST_EXPR.ordinal(), Token.COLON.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STATEMENT -> DEFAULT COLON (81)
        right = getProductionRight(new int[]{Token.DEFAULT.ordinal(), Token.COLON.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STATEMENT -> BREAK SEMI; (82)
        right = getProductionRight(new int[]{Token.BREAK.ordinal(), Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);
    }

    private void initFunctionDefinitionWithLoop() {
        //STATEMENT -> WHILE LP TEST RP STATEMENT (83)
        ArrayList<Integer> right = null;
        Production production = null;
        right = getProductionRight(new int[]{Token.WHILE.ordinal(), Token.LP.ordinal(), Token.TEST.ordinal(),
                Token.RP.ordinal(), Token.STATEMENT.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STATEMENT -> FOR LP OPT_EXPR  TEST SEMI END_OPT_EXPR RP STATEMENT(84)
        right = getProductionRight(new int[]{Token.FOR.ordinal(), Token.LP.ordinal(), Token.OPT_EXPR.ordinal(),
                Token.TEST.ordinal(), Token.SEMI.ordinal(), Token.END_OPT_EXPR.ordinal(), Token.RP.ordinal(),
                Token.STATEMENT.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //OPT_EXPR -> EXPR SEMI(85)
        right = getProductionRight(new int[]{Token.EXPR.ordinal(), Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.OPT_EXPR.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //OPT_EXPR -> SEMI(86)
        right = getProductionRight(new int[]{Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.OPT_EXPR.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //END_OPT_EXPR -> EXPR(87)
        right = getProductionRight(new int[]{Token.EXPR.ordinal()});
        production = new Production(productionNum, Token.END_OPT_EXPR.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STATEMENT -> DO STATEMENT WHILE LP TEST RP SEMI(88)
        right = getProductionRight(new int[]{Token.DO.ordinal(), Token.STATEMENT.ordinal(), Token.WHILE.ordinal(),
                Token.LP.ordinal(), Token.TEST.ordinal(), Token.RP.ordinal(), Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

    }

    private void initComputingOperation() {
        //BINARY -> BINARY STAR BINARY(89)
        ArrayList<Integer> right = null;
        Production production = null;
        right = getProductionRight(new int[]{Token.BINARY.ordinal(), Token.STAR.ordinal(), Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //BINARY -> BINARY DIVOP BINARY(90)
        right = getProductionRight(new int[]{Token.BINARY.ordinal(), Token.DIVOP.ordinal(), Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //BINARY -> BINARY SHIFTOP BINARY(91)
        right = getProductionRight(new int[]{Token.BINARY.ordinal(), Token.SHIFTOP.ordinal(), Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //BINARY -> BINARY AND BINARY(92)
        right = getProductionRight(new int[]{Token.BINARY.ordinal(), Token.AND.ordinal(), Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //BINARY -> BINARY XOR BINARY(93)
        right = getProductionRight(new int[]{Token.BINARY.ordinal(), Token.XOR.ordinal(), Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //BINARY -> BINARY PLUS BINARY(94)
        right = getProductionRight(new int[]{Token.BINARY.ordinal(), Token.PLUS.ordinal(), Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //BINARY -> BINARY MINUS BINARY(95)
        right = getProductionRight(new int[]{Token.BINARY.ordinal(), Token.MINUS.ordinal(), Token.BINARY.ordinal()});
        production = new Production(productionNum, Token.BINARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> UNARY INCOP i++ (96)
        right = getProductionRight(new int[]{Token.UNARY.ordinal(), Token.INCOP.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> INCOP UNARY ++i (97)
        right = getProductionRight(new int[]{Token.INCOP.ordinal(), Token.UNARY.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> MINUS UNARY  a = -a (98)
        right = getProductionRight(new int[]{Token.MINUS.ordinal(), Token.UNARY.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> STAR UNARY b = *a (99)
        right = getProductionRight(new int[]{Token.STAR.ordinal(), Token.UNARY.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> UNARY STRUCTOP NAME  a = tag->name (100)
        right = getProductionRight(new int[]{Token.UNARY.ordinal(), Token.STRUCTOP.ordinal(),
                Token.NAME.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> UNARY LB EXPR RB b = a[2]; (101)
        right = getProductionRight(new int[]{Token.UNARY.ordinal(), Token.LB.ordinal(),
                Token.EXPR.ordinal(), Token.RB.ordinal()});

        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> UNARY  LP ARGS RP  fun(a, b ,c) (102)
        //change
        right = getProductionRight(new int[]{Token.UNARY.ordinal(), Token.LP.ordinal(),
                Token.ARGS.ordinal(), Token.RP.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> UNARY LP RP  fun() (103)
        right = getProductionRight(new int[]{Token.UNARY.ordinal(), Token.LP.ordinal(),
                Token.RP.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //ARGS -> NO_COMMA_EXPR (104)
        right = getProductionRight(new int[]{Token.NO_COMMA_EXPR.ordinal()});
        production = new Production(productionNum, Token.ARGS.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //ARGS -> NO_COMMA_EXPR COMMA ARGS (105)
        right = getProductionRight(new int[]{Token.NO_COMMA_EXPR.ordinal(), Token.COMMA.ordinal(),
                Token.ARGS.ordinal()});
        production = new Production(productionNum, Token.ARGS.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

    }

    private void initRemaindingProduction() {
        //STATEMENT -> TARGET COLON STATEMENT
        ArrayList<Integer> right = null;
        Production production = null;
        right = getProductionRight(new int[]{Token.TARGET.ordinal(), Token.COLON.ordinal(), Token.STATEMENT.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STATEMENT -> GOTO TARGET SEMI
        right = getProductionRight(new int[]{Token.GOTO.ordinal(), Token.TARGET.ordinal(), Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //TARGET -> NAME
        right = getProductionRight(new int[]{Token.NAME.ordinal()});
        production = new Production(productionNum, Token.TARGET.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //VAR_DECL -> VAR_DECL LB CONST_EXPR RB  a[5] (109)
        right = getProductionRight(new int[]{Token.VAR_DECL.ordinal(), Token.LB.ordinal(), Token.CONST_EXPR.ordinal(),
                Token.RB.ordinal()});
        production = new Production(productionNum, Token.VAR_DECL.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //COMPOUND_STMT-> LC  STMT_LIST RC(110)
        /*
         * 函数定义：bar() { foo();}
         * 运行函数定义时没有变量声明
         */
        right = getProductionRight(new int[]{Token.LC.ordinal(),
                Token.STMT_LIST.ordinal(), Token.RC.ordinal()});
        production = new Production(productionNum, Token.COMPOUND_STMT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //STATEMENT -> RETURN SEMI (111)
        right = getProductionRight(new int[]{Token.RETURN.ordinal(),
                Token.SEMI.ordinal()});
        production = new Production(productionNum, Token.STATEMENT.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> LP EXPR RP (112)
        right = getProductionRight(new int[]{Token.LP.ordinal(),
                Token.EXPR.ordinal(), Token.RP.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);

        //UNARY -> UNARY DECOP i-- (113)
        right = getProductionRight(new int[]{Token.UNARY.ordinal(), Token.DECOP.ordinal()});
        production = new Production(productionNum, Token.UNARY.ordinal(), 0, right);
        productionNum++;
        addProduction(production, false);


    }

    private void addProduction(Production production, boolean nullable) {
        int left = production.getLeft();
        ArrayList<Production> productionList = productionMap.computeIfAbsent(left, k -> new ArrayList<>());

        if (!productionList.contains(production)) {
            productionList.add(production);
        }

        addSymbolMapAndArray(production, nullable);

    }

    private void addSymbolMapAndArray(Production production, boolean nullable) {
        //add Symbol array and Symbol map
        int[] right = new int[production.getRight().size()];
        for (int i = 0; i < right.length; i++) {
            right[i] = production.getRight().get(i);
        }

        if (symbolMap.containsKey(production.getLeft())) {
            symbolMap.get(production.getLeft()).addProduction(right);
        } else {
            ArrayList<int[]> productions = new ArrayList<>();
            productions.add(right);
            Symbols symObj = new Symbols(production.getLeft(), nullable, productions);
            symbolMap.put(production.getLeft(), symObj);
            symbolArray.add(symObj);
        }
    }

    private void addTerminalToSymbolMapAndArray() {
        for (int i = Token.FIRST_TERMINAL_INDEX; i <= Token.LAST_TERMINAL_INDEX; i++) {
            Symbols symObj = new Symbols(i, false, null);
            symbolMap.put(i, symObj);
            symbolArray.add(symObj);
        }
    }

    private ArrayList<Integer> getProductionRight(int[] arr) {
        ArrayList<Integer> right = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            right.add(arr[i]);
        }

        return right;
    }

    public HashMap<Integer, Symbols> getSymbolMap() {
        return symbolMap;
    }

    public ArrayList<Symbols> getSymbolArray() {
        return symbolArray;
    }

    public HashMap<Integer, ArrayList<Production>> getProductionMap() {
        return productionMap;
    }

}
