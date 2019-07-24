package symboltable;

import debug.ConsoleDebugColor;
import parse.LRStateTableParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author dejavudwh isHudw
 */

public class TypeSystem {
    private static TypeSystem instance;

    private HashMap<String, ArrayList<Symbol>> symbolTable = new HashMap<>();
    private HashMap<String, StructDefine> structTable = new HashMap<>();

    private TypeSystem() {}

    public static TypeSystem getInstance() {
        if (instance == null) {
            instance = new TypeSystem();
        }

        return instance;
    }

    public Symbol newSymbol(String name, int level) {
        return new Symbol(name, level);
    }

    public TypeLink newType(String typeText) {
        Specifier sp;
        int type = Specifier.NONE;
        boolean isLong = false, isSigned = true;
        switch (typeText.charAt(0)) {
            case 'c':
                if (typeText.charAt(1) == 'h') {
                    type = Specifier.CHAR;
                }
                break;
            case 'd':
            case 'f':
                System.err.println("Floating point Numbers are not supported");
                System.exit(1);
                break;
            case 'i':
                type = Specifier.INT;
                break;
            case 'l':
                isLong = true;
                break;
            case 'u':
                isSigned = false;
                break;
            case 'v':
                if (typeText.charAt(2) == 'i') {
                    type = Specifier.VOID;
                }
                break;
            case 's':
                //ignore short signed
                break;
            default:
                break;
        }

        sp = new Specifier();
        sp.setType(type);
        sp.setLong(isLong);
        sp.setSign(isSigned);

        TypeLink link = new TypeLink(false, false, sp);

        return link;
    }

    public TypeLink newClass(String classText) {
        Specifier sp = new Specifier();
        sp.setType(Specifier.NONE);
        setClassType(sp, classText.charAt(0));

        TypeLink link = new TypeLink(false, false, sp);
        return link;
    }

    private void setClassType(Specifier sp, char c) {
        switch(c) {
            case 0:
                sp.setStorageClass(Specifier.FIXED);
                sp.setStatic(false);
                sp.setExternal(false);
                break;
            case 't':
                sp.setStorageClass(Specifier.TYPEDEF);
                break;
            case 'r':
                sp.setStorageClass(Specifier.REGISTER);
                break;
            case 's':
                sp.setStatic(true);
                break;
            case 'e':
                sp.setExternal(true);
                break;
            default:
                System.err.println("Internal error, Invalid Class type");
                System.exit(1);
                break;
        }
    }

    public Declarator addDeclarator(Symbol symbol, int declaratorType) {
        Declarator declarator = new Declarator(declaratorType);
        TypeLink link = new TypeLink(true, false, declarator);
        symbol.addDeclarator(link);

        return declarator;
    }

    public void addSpecifierToDeclaration(TypeLink specifier, Symbol symbol) {
        while (symbol != null) {
            symbol.addSpecifier(specifier);
            symbol = symbol.getNextSymbol();
        }
    }

    public void specifierCopy(Specifier dst, Specifier org) {
        dst.setConstantValue(org.getConstantValue());
        dst.setExternal(org.isExternal());
        dst.setLong(org.isLong());
        dst.setOutputClass(org.getOutputClass());
        dst.setSign(org.isSign());
        dst.setStatic(org.isStatic());
        dst.setStorageClass(org.getStorageClass());
    }

    public ArrayList<Symbol> getSymbol(String text) {
        return symbolTable.get(text);
    }

    public Symbol getSymbolByText(String text, int level, String scope) {
        if (scope.equals(text)) {
            scope = LRStateTableParser.GLOBAL_SCOPE;
        }

        ArrayList<Symbol> symbolList = instance.getSymbol(text);
        int i = 0;
        Symbol symbol = null;

        while (i < symbolList.size()) {

            if (symbol == null && symbolList.get(i).getScope().equals(LRStateTableParser.GLOBAL_SCOPE)) {
                symbol = symbolList.get(i);
            }

            if (symbolList.get(i).getScope().equals(scope)) {
                symbol = symbolList.get(i);
            }

            if (symbol != null && symbolList.get(i).getLevel() >= level) {
                symbol = symbolList.get(i);
            }

            i++;
        }

        return symbol;
    }

    public void addSymbolsToTable(Symbol headSymbol, String scope) {
        while (headSymbol != null) {
            headSymbol.addScope(scope);

            ArrayList<Symbol> symList = symbolTable.get(headSymbol.name);
            if (symList == null) {
                symList = new ArrayList<>();
                symList.add(headSymbol);
                symbolTable.put(headSymbol.name, symList);
            }
            else {
                handleDuplicateSymbol(headSymbol, symList);
            }

            headSymbol = headSymbol.getNextSymbol();
        }
    }

    private void handleDuplicateSymbol(Symbol symbol, ArrayList<Symbol> symList) {
        boolean harmless = true;
        Iterator it = symList.iterator();

        while (it.hasNext()) {
            Symbol sym = (Symbol)it.next();
            if (sym.equals(symbol)) {
                System.err.println("Symbol definition replicate: " + sym.name);
                System.exit(1);
            }
        }

        if (harmless) {
            symList.add(symbol);
        }
    }

}
