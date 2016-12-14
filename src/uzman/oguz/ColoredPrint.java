package uzman.oguz;

/**
 * Created by oguz on 14/12/2016.
 */
public class ColoredPrint {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void print(String color, Object... objects ){
        StringBuilder stringBuilder = new StringBuilder(color);
        for (int i = 0; i < objects.length; i++) {
            stringBuilder.append(objects[i]);
        }
        stringBuilder.append(ANSI_RESET);
        System.out.println(stringBuilder);
    }
}
