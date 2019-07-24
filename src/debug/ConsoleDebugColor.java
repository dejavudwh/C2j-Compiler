package debug;

public class ConsoleDebugColor {
    public static final boolean DEBUG = true;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void outlnBlue(String text) {
        if (DEBUG) {
            if (DEBUG) {
                System.out.println(ANSI_BLUE + text + ANSI_RESET);
            }
        }
    }

    public static void outlnRed(String text) {
        if (DEBUG) {
            System.out.println(ANSI_RED + text + ANSI_RED);
        }
    }

    public static void outlnYellow(String text) {
        if (DEBUG) {
            System.out.println(ANSI_YELLOW + text + ANSI_RESET);
        }
    }

    public static void outlnBlack(String text) {
        if (DEBUG) {
            System.out.println(ANSI_BLACK + text + ANSI_RESET);
        }
    }

    public static void outlnWhite(String text) {
        if (DEBUG) {
            System.out.println(ANSI_WHITE + text + ANSI_RESET);
        }
    }

    public static void outlnGreen(String text) {
        if (DEBUG) {
            System.out.println(ANSI_GREEN + text + ANSI_RESET);
        }
    }

    public static void outlnPurple(String text) {
        if (DEBUG) {
            System.out.println(ANSI_PURPLE + text + ANSI_RESET);
        }
    }

    public static void outlnCyan(String text) {
        if (DEBUG) {
            System.out.println(ANSI_CYAN + text + ANSI_RESET);
        }
    }

    public static void outPurple(String text) {
        if (DEBUG) {
            System.out.print(ANSI_PURPLE + text + ANSI_RESET);
        }
    }

    public static void outGreen(String text) {
        if (DEBUG) {
            System.out.print(ANSI_GREEN + text + ANSI_RESET);
        }
    }

    public static void outCyan(String text) {
        if (DEBUG) {
            System.out.print(ANSI_CYAN + text + ANSI_RESET);
        }
    }

    public static void outBlue(String text) {
            if (DEBUG) {
                System.out.print(ANSI_BLUE + text + ANSI_RESET);
            }
    }

}
