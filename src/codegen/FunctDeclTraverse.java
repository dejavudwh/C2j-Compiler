package codegen;

import ast.AstNode;
import ast.NodeKey;
import interpreter.FunctionArgumentList;
import parse.SyntaxProductionInit;
import symboltable.Declarator;
import symboltable.Specifier;
import symboltable.Symbol;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author dejavudwh isHudw
 */

public class FunctDeclTraverse extends BaseTraverse {
    private ArrayList<Object> argsList = null;
    private AstNode currentNode;
    Generator generator = Generator.getInstance();

    @Override
    public Object traverse(AstNode root) {
        int production = (Integer) root.getAttribute(NodeKey.PRODUCTION);
        Symbol symbol;
        currentNode = root;

        switch (production) {
            case SyntaxProductionInit.NewName_LP_RP_TO_FunctDecl:
                root.reverseChildren();
                AstNode n = root.getChildren().get(0);
                String name = (String) n.getAttribute(NodeKey.TEXT);
                symbol = (Symbol) root.getAttribute(NodeKey.SYMBOL);
                generator.setCurrentFuncName(name);
                if (name != null && !name.equals("main")) {
                    String declaration = name + emitArgs(symbol);
                    generator.emitDirective(Directive.METHOD_PUBBLIC_STATIC, declaration);
                    generator.setNameAndDeclaration(name, declaration);
                }
                copyChild(root, root.getChildren().get(0));
                break;

            case SyntaxProductionInit.NewName_LP_VarList_RP_TO_FunctDecl:
                n = root.getChildren().get(0);
                name = (String) n.getAttribute(NodeKey.TEXT);
                symbol = (Symbol) root.getAttribute(NodeKey.SYMBOL);
                generator.setCurrentFuncName(name);
                if (name != null && !name.equals("main")) {
                    String declaration = name + emitArgs(symbol);
                    generator.emitDirective(Directive.METHOD_PUBBLIC_STATIC, declaration);
                    generator.setNameAndDeclaration(name, declaration);
                }

                Symbol args = symbol.getArgList();
                initArgumentList(args);

                if (args == null || argsList == null || argsList.isEmpty()) {
                    System.err.println("Execute function with arg list but arg list is null");
                    System.exit(1);
                }
                break;

            default:
                break;
        }
        return root;
    }

    private void initArgumentList(Symbol args) {
        if (args == null) {
            return;
        }

        generator.initFuncArguments(true);
        argsList = FunctionArgumentList.getInstance().getFuncArgList(true);
        Symbol eachSym = args;
        int count = 0;

        while (eachSym != null && eachSym.getDeclarator(Declarator.POINTER) != null) {
            ValueSetter setter = (ValueSetter) eachSym;
            try {
                setter.setValue(argsList.get(count));
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }

            eachSym = eachSym.getNextSymbol();
        }
        generator.initFuncArguments(false);
    }


    private String emitArgs(Symbol funSymbol) {
        Symbol s = funSymbol.getArgList();
        ArrayList<Symbol> params = new ArrayList<>();
        while (s != null) {
            params.add(s);
            s = s.getNextSymbol();
        }
        Collections.reverse(params);
        String args = "(";
        for (int i = 0; i < params.size(); i++) {
            Symbol symbol = params.get(i);
            String arg = "";
            if (symbol.getDeclarator(Declarator.ARRAY) != null || symbol.getDeclarator(Declarator.POINTER) != null) {
                arg += "[";
            }

            if (symbol.hasType(Specifier.INT)) {
                arg += "I";
            }

            args += arg;
        }

        if (funSymbol.hasType(Specifier.INT)) {
            args += ")I";
        } else {
            args += ")V";
        }

        return args;
    }
}
