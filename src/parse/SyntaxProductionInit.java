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
    private static SyntaxProductionInit instance = null;
    private int productionNum = 0;
    private HashMap<Integer, ArrayList<Production>> productionMap = new HashMap<>();
    private HashMap<Integer, Symbols> symbolMap = new HashMap<>();
    private ArrayList<Symbols> symbolArray = new ArrayList<>();

    private SyntaxProductionInit() {
        initVariableDeclaration();
        addTerminalToSymbolMapAndArray();
    }

    public static SyntaxProductionInit getInstance() {
        if (instance == null) {
            instance = new SyntaxProductionInit();
        }

        return instance;
    }

    private void initVariableDeclaration() {
        /*LB: { RB:}
         *
         * C variable declaration grammar
         *  PROGRAM -> EXT_DEF_LIST
         *
         *  EXT_DEF_LIST -> EXT_DEF_LIST EXT_DEF
         *
         *  EXT_DEF -> OPT_SPECIFIERS EXT_DECL_LIST SEMI
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
        ArrayList<Integer> right;
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

    private void addProduction(Production production, boolean nullable) {
        int left = production.getLeft();
        ArrayList<Production> productionList = productionMap.computeIfAbsent(left, k -> new ArrayList<>());

        if (!productionList.contains(production)) {
            productionList.add(production);
        }

        addSymbolMapAndArray(production, nullable);

    }

    private void addSymbolMapAndArray(Production production, boolean nullable) {
        //add symbol array and symbol map
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
