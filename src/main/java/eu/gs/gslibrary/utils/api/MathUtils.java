package eu.gs.gslibrary.utils.api;
import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class MathUtils {
    private static final Random rand = new Random();

    /**
     * @return random int between min and max
     */

    public static int randomInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * @return random boolean value
     */

    public static boolean randomBool() {
        return rand.nextBoolean();
    }

    /**
     * @return random float between 0 and 1
     */

    public static float randomFloat() {
        Random rnd = new Random();
        return rnd.nextFloat();
    }

    /**
     * @return Converted seconds to minutes and seconds
     */

    public static String secToMin(int sec) {
        if (sec == 0) return "00:00";
        String s1 = Integer.toString(sec / 60);
        String s2 = Integer.toString(sec - (sec / 60) * 60);
        if (s1.length() == 1) s1 = "0" + s1;
        if (s2.length() == 1) s2 = "0" + s2;
        return s1 + ":" + s2;
    }
}
