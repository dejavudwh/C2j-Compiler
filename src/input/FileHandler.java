package input;

/**
 * FileHandler
 *
 * @author dejavudwh
 */

public interface FileHandler {
    /**
     * 作为获取输入的接口，输入可以来自磁盘也可以来自控制台输入
     */
    void open();
    int close();
    int read(byte[] buf, int begin, int end);
    StringBuffer getSourceCode();
}
