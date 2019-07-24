package debug;

import java.io.File;

public class TestIO {
    public static void main(String[] args) {
        File file = new File("lrStateTable.sb");
        System.out.println(file.exists());
    }
}
