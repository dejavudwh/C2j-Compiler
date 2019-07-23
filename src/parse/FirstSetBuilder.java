package parse;

import lexer.Token;
import debug.ConsoleDebugColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * first set builder
 * @author dejavudwh isHudw
 */

public class FirstSetBuilder {
    private HashMap<Integer, Symbols> symbolMap = new HashMap<>();
    private ArrayList<Symbols> symbolArray = new ArrayList<>();
    private boolean runFirstSetPass = true;

    public FirstSetBuilder() {
        initialize();
    }

    private void initialize() {
        symbolMap = SyntaxProductionInit.getInstance().getSymbolMap();
        symbolArray = SyntaxProductionInit.getInstance().getSymbolArray();
    }

    public void buildFirstSets() {
        while (runFirstSetPass) {
            runFirstSetPass = false;

            Iterator<Symbols> it = symbolArray.iterator();
            while (it.hasNext()) {
                Symbols symbol = it.next();
                addSymbolFirstSet(symbol);
            }
        }

        ConsoleDebugColor.outlnPurple("First sets :");
        debugPrintAllFirstSet();
        ConsoleDebugColor.outlnPurple("First sets end");

    }

    private void addSymbolFirstSet(Symbols symbol) {
        if (Token.isTerminal(symbol.value)) {
            if (!symbol.firstSet.contains(symbol.value)) {
                symbol.firstSet.add(symbol.value);
            }

            return ;
        }

        ArrayList<int[]> productions = symbol.productions;
        for (int[] rightSize : productions) {
            if (rightSize.length == 0) {
                continue;
            }

            if (Token.isTerminal(rightSize[0]) && !symbol.firstSet.contains(rightSize[0])) {
                runFirstSetPass = true;
                symbol.firstSet.add(rightSize[0]);
            } else if (!Token.isTerminal(rightSize[0])) {
                int pos = 0;
                Symbols curSymbol;
                do {
                    curSymbol = symbolMap.get(rightSize[pos]);
                    if (!symbol.firstSet.containsAll(curSymbol.firstSet)) {
                        runFirstSetPass = true;

                        for (int j = 0; j < curSymbol.firstSet.size(); j++) {
                            if (!symbol.firstSet.contains(curSymbol.firstSet.get(j))) {
                                symbol.firstSet.add(curSymbol.firstSet.get(j));
                            }
                        }
                    }
                    pos++;
                } while (pos < rightSize.length && curSymbol.isNullable);
            }
        }
    }

    public ArrayList<Integer> getFirstSet(int symbol) {
        Iterator<Symbols> it = symbolArray.iterator();
        while (it.hasNext()) {
            Symbols sym = it.next();
            if (sym.value == symbol) {
                return sym.firstSet;
            }
        }

        return null;
    }

    public boolean isSymbolNullable(int sym) {
        Symbols symbol= symbolMap.get(sym);
        if (symbol == null) {
            return false;
        }

        return symbol.isNullable;
    }

    private void debugPrintAllFirstSet() {
        if (ConsoleDebugColor.DEBUG) {
            Iterator<Symbols> it = symbolArray.iterator();
            while (it.hasNext()) {
                Symbols sym = it.next();
                printFirstSet(sym);
            }
        }
    }

    private void printFirstSet(Symbols symbol) {
        String s = Token.getTokenStr(symbol.value);
        s += "{ ";
        for (int i = 0; i < symbol.firstSet.size(); i++) {
            s += Token.getTokenStr(symbol.firstSet.get(i)) + " ";
        }
        s += " }";

        ConsoleDebugColor.outlnCyan(s);
        System.out.println("============");
    }
}
