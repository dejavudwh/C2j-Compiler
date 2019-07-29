package interpreter;

import ast.AstNode;
import ast.NodeKey;
import debug.ConsoleDebugColor;
import parse.SyntaxProductionInit;
import symboltable.Symbol;

/**
 * @author dejavudwh isHudw
 */

public class BinaryExecutor extends BaseExecutor {

    @Override
    public Object execute(AstNode root) {
        executeChildren(root);

        AstNode child;
        int production = (int) root.getAttribute(NodeKey.PRODUCTION);
        switch (production) {
            case SyntaxProductionInit.Uanry_TO_Binary:
                child = root.getChildren().get(0);
                copyChild(root, child);
                break;

            case SyntaxProductionInit.Binary_Plus_Binary_TO_Binary:
            case SyntaxProductionInit.Binary_DivOp_Binary_TO_Binary:
            case SyntaxProductionInit.Binary_Minus_Binary_TO_Binary:
                int val1 = (Integer) root.getChildren().get(0).getAttribute(NodeKey.VALUE);
                int val2 = (Integer) root.getChildren().get(1).getAttribute(NodeKey.VALUE);
                if (production == SyntaxProductionInit.Binary_Plus_Binary_TO_Binary) {
                    String text = root.getChildren().get(0).getAttribute(NodeKey.TEXT) + " plus " + root.getChildren().get(1).getAttribute(NodeKey.TEXT);
                    root.setAttribute(NodeKey.VALUE, val1 + val2);
                    root.setAttribute(NodeKey.TEXT, text);
                    ConsoleDebugColor.outlnPurple(text + " is " + (val1 + val2));
                } else if (production == SyntaxProductionInit.Binary_Minus_Binary_TO_Binary) {
                    String text = root.getChildren().get(0).getAttribute(NodeKey.TEXT) + " minus " + root.getChildren().get(1).getAttribute(NodeKey.TEXT);
                    root.setAttribute(NodeKey.VALUE, val1 - val2);
                    root.setAttribute(NodeKey.TEXT, text);
                    ConsoleDebugColor.outlnPurple(text + " is " + (val1 - val2));
                } else {
                    root.setAttribute(NodeKey.VALUE, val1 / val2);
                    ConsoleDebugColor.outlnPurple(root.getChildren().get(0).getAttribute(NodeKey.TEXT) + " is divided by "
                            + root.getChildren().get(1).getAttribute(NodeKey.TEXT) + " and result is " + (val1 / val2));
                }
                break;

            case SyntaxProductionInit.Binary_RelOP_Binary_TO_Binray:
                val1 = (Integer) root.getChildren().get(0).getAttribute(NodeKey.VALUE);
                String operator = (String) root.getChildren().get(1).getAttribute(NodeKey.TEXT);
                val2 = (Integer) root.getChildren().get(2).getAttribute(NodeKey.VALUE);

                switch (operator) {
                    case "==":
                        root.setAttribute(NodeKey.VALUE, val1 == val2 ? 1 : 0);
                        break;

                    case "<":
                        root.setAttribute(NodeKey.VALUE, val1 < val2 ? 1 : 0);
                        break;

                    case "<=":
                        root.setAttribute(NodeKey.VALUE, val1 <= val2 ? 1 : 0);
                        break;

                    case ">":
                        root.setAttribute(NodeKey.VALUE, val1 > val2 ? 1 : 0);
                        break;

                    case ">=":
                        root.setAttribute(NodeKey.VALUE, val1 >= val2 ? 1 : 0);
                        break;

                    case "!=":
                        root.setAttribute(NodeKey.VALUE, val1 != val2 ? 1 : 0);
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;

        }

        return root;
    }
}
