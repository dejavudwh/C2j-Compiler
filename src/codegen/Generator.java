package codegen;

/**
 * @author dejavudwh isHudw
 */

public class Generator extends CodeGenerator {
    private static Generator instance = null;

    private Generator() {

    }

    public static Generator getInstance() {
        if (instance == null) {
            instance = new Generator();
        }

        return instance;
    }
}
