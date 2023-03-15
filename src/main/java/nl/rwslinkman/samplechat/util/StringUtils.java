package nl.rwslinkman.samplechat.util;

import java.util.Random;

public class StringUtils {

    public static boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    public static String generateRandom(int length) {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for(int i = 0; i < length; i++) {
            sb.append(allowedChars.charAt(random.nextInt(length)));
        }
        return sb.toString();
    }
}
