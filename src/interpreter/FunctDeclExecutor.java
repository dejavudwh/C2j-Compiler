package interpreter;

import ast.AstNode;
import ast.NodeKey;
import parse.SyntaxProductionInit;
import symboltable.Symbol;

import java.util.ArrayList;

public class FunctDeclExecutor extends BaseExecutor {
    private ArrayList<Object> argsList = null;
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
                //获得参数列表
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

        argsList = FunctionArgumentList.getInstance().getFuncArgList(true);
        Symbol eachSym = args;
        int count = 0;
        while (eachSym != null) {
            ValueSetter setter = (ValueSetter) eachSym;
            try {
                setter.setValue(argsList.get(count));
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }

            eachSym = eachSym.getNextSymbol();
        }
    }

}
