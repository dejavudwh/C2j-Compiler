package symboltable;

import java.lang.reflect.Type;

/**
 *
 * @author dejavudwh isHudw
 */

public class TypeSystem {
    private static TypeSystem instance;

    private TypeSystem() {}

    public static TypeSystem getInstance() {
        if (instance == null) {
            instance = new TypeSystem();
        }

        return instance;
    }

}
