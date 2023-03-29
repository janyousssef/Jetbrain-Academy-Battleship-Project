package jan;

import java.io.IOException;

public class Utils {
    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            var ignored = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
