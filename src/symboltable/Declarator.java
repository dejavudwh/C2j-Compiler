package symboltable;

import java.util.HashMap;

/**
 * A modifier for a symbol array/pointer/func
 *
 * @author dejavdwh isHudw
 */

public class Declarator {
    public static int POINTER = 0;
    public static int ARRAY = 1;
    public static int FUNCTION = 2;

    private int declareType;
    private int numberOfElements = 0;

    HashMap<Integer, Object> elements = null;

    public Declarator(int type) {
        this.declareType = type;
    }

    public int getType() {
        return declareType;
    }

    public int getElementNum() {
        return numberOfElements;
    }

    public void setElementNum(int num) {
        if (num < 0) {
            numberOfElements = 0;
        } else {
            numberOfElements = num;
            elements = new HashMap<>();
        }
    }

    public Object getElement(int index) throws Exception {
        if (elements == null || index >= numberOfElements) {
            throw new Exception("element list is null or index out of range");
        }

        return elements.get(index);
    }

    public void addElement(int index, Object obj) throws Exception {
        if (elements == null) {
            throw new Exception("This is not an Array!");
        }

        if (index >= numberOfElements) {
            throw new Exception("Array Out Of Range");
        }

        if (elements != null) {
            elements.put(index, obj);
        }
    }

}