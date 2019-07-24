package symboltable;

/**
 * Symbol specifier
 *
 * @author dejavudwh isHudw
 */

public class Specifier {
    /**
     * Variable types
     */
    public static int NONE = -1;
    public static int INT = 0;
    public static int CHAR = 1;
    public static int VOID = 2;
    public static int STRUCTURE = 3;
    public static int LABEL = 4;

    /**
     * storage
     */
    public static int FIXED = 0;
    public static int REGISTER = 1;
    public static int AUTO = 2;
    public static int TYPEDEF = 3;
    public static int CONSTANT = 4;

    public static int NO_OCLASS = 0;
    public static int PUBLIC = 1;
    public static int PRIVATE = 2;
    public static int EXTERN = 3;
    public static int COMMON = 4;

    private int basicType;
    private int storageClass;
    private int outputClass = NO_OCLASS;
    private boolean isLong = false;
    private boolean isSigned = false;
    private boolean isStatic = false;
    private boolean isExternal = false;
    private int constantValue = 0;

    public int getType() {
        return basicType;
    }

    public int getStorageClass() {
        return storageClass;
    }

    public int getOutputClass() {
        return outputClass;
    }

    public boolean isLong() {
        return isLong;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isExternal() {
        return isExternal;
    }

    public void setType(int basicType) {
        this.basicType = basicType;
    }

    public void setStorageClass(int storageClass) {
        this.storageClass = storageClass;
    }

    public void setOutputClass(int outputClass) {
        this.outputClass = outputClass;
    }

    public void setLong(boolean aLong) {
        isLong = aLong;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public void setExternal(boolean external) {
        isExternal = external;
    }

    public void setConstantValue(int constantValue) {
        this.constantValue = constantValue;
    }

}
