package interpreter;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author dejavudwh isHudw
 */

public class FunctionArgumentList {
    private static FunctionArgumentList instance = null;
    private ArrayList<Object> funcArgList = new ArrayList<>();
    private ArrayList<Object> argSymList = new ArrayList<>();

    public static FunctionArgumentList getInstance() {
        if (instance == null) {
            instance = new FunctionArgumentList();
        }

        return instance;
    }

    public void setFuncArgList(ArrayList<Object> list) {
        funcArgList = list;
    }

    public void setFuncArgSymbolList(ArrayList<Object> list) {
        this.argSymList = list;
    }

    public ArrayList<Object> getFuncArgList(boolean reverse) {
        if (reverse) {
            Collections.reverse(funcArgList);
        }

        return funcArgList;
    }

    public ArrayList<Object> getFuncArgSymsList(boolean reverse) {
        if (reverse) {
            Collections.reverse(argSymList);
        }

        return argSymList;
    }

    private FunctionArgumentList() {
    }
}
