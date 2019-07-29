package interpreter;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;
import symboltable.Declarator;
import symboltable.Symbol;
import symboltable.TypeLink;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;

/**
 *
 * @author dejavudwh isHudw
 */

public class FunctDeclExecutor extends BaseExecutor {
    private ArrayList<Object> argsList = null;
    private ArrayList<Object> argsSymList = null;
    private AstNode currentNode;

    @Override
    public Object execute(AstNode root) {
        int production = (Integer) root.getAttribute(NodeKey.PRODUCTION);
        Symbol symbol;
        currentNode = root;

        switch (production) {
            case SyntaxProductionInit.NewName_LP_RP_TO_FunctDecl:
                root.reverseChildren();
                copyChild(root, root.getChildren().get(0));
                break;

            case SyntaxProductionInit.NewName_LP_VarList_RP_TO_FunctDecl:
                symbol = (Symbol) root.getAttribute(NodeKey.SYMBOL);

                Symbol args = symbol.getArgList();
                initArgumentList(args);

                if (args == null || argsList == null || argsList.isEmpty()) {
                    System.err.println("generate function with arg list but arg list is null");
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

        argsList = FunctionArgumentList.getInstance().getFuncArgList(true);
        argsSymList = FunctionArgumentList.getInstance().getFuncArgSymsList(true);
        Symbol eachSym = args;
        int count = 0;
        int count2 = 1;
        while (eachSym != null) {
            ValueSetter setter = (ValueSetter) eachSym;
            try {
                setter.setValue(argsList.get(count));
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (eachSym.getDeclarator(Declarator.ARRAY) != null) {
                eachSym.addDeclarator(new TypeLink(true, false, ((Symbol) argsSymList.get(count2)).getDeclarator(Declarator.ARRAY)));
                count2++;
            }

            eachSym = eachSym.getNextSymbol();
        }
    }

}
