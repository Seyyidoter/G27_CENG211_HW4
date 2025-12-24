package util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Random helper methods.
 * Use this class for all random values in the game.
 */
public final class RandomUtil {

    private RandomUtil() {
        // no object
    }

    /**
     * Random int in [min, max].
     */
    public static int generateRandomNumber(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * True with given percent chance (0..100).
     */
    public static boolean isChanceSuccess(int percentage) {
        if (percentage <= 0) return false;
        if (percentage >= 100) return true;
        return ThreadLocalRandom.current().nextInt(1, 101) <= percentage; // 1..100
    }

    /**
     * Pick a random element from an array.
     * Returns null if array is null or empty.
     */
    public static <T> T pickRandomElement(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(array.length);
        return array[index];
    }

    /**
     * Random letter from A to H (inclusive).
     */
    public static char randomLetterAH() {
        return (char) generateRandomNumber('A', 'H');
    }
}
