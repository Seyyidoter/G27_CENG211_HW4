package util;

import java.util.Random;

/**
 * Random helper methods.
 * Uses strict java.util.Random to comply with assignment rules.
 */
public final class RandomUtil {

    private static final Random random = new Random();
    private static final int FACE_COUNT = 6;
    private static final int LETTER_COUNT = 8; // A..H
    private static final int MAX_SAME_LETTER = 2;

    private RandomUtil() {
        // utility class, no instance allowed
    }

    /**
     * Random int in [min, max] (inclusive).
     */
    public static int generateRandomNumber(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * True with given percent chance (0..100).
     */
    public static boolean isChanceSuccess(int percentage) {
        if (percentage <= 0) return false;
        if (percentage >= 100) return true;
        return random.nextInt(100) < percentage; // 0..99
    }

    /**
     * Pick a random element from an array.
     * Returns null if array is null or empty.
     */
    public static <T> T pickRandomElement(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[random.nextInt(array.length)];
    }

    /**
     * Random letter from A to H (inclusive).
     */
    public static char randomLetterAH() {
        return (char) generateRandomNumber('A', 'H');
    }

    /**
     * Generates 6 letters for a box.
     * Rule: same letter cannot appear more than 2 times.
     */
    public static char[] generateValidSurfaces() {
        // try random generation first
        final int maxAttempts = 5000;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            char[] surfaces = new char[FACE_COUNT];

            for (int i = 0; i < FACE_COUNT; i++) {
                surfaces[i] = randomLetterAH();
            }

            if (isValidCombination(surfaces)) {
                return surfaces;
            }
        }

        // fallback: always valid (A,A,B,B,C,C) and shuffle
        char[] fallback = new char[] { 'A', 'A', 'B', 'B', 'C', 'C' };
        shuffleChars(fallback);
        return fallback;
    }

    // Helper to check the validation
    private static boolean isValidCombination(char[] surfaces) {
        if (surfaces == null || surfaces.length != FACE_COUNT) {
            return false;
        }

        int[] counts = new int[LETTER_COUNT]; // A..H
        for (char c : surfaces) {
            if (c < 'A' || c > 'H') {
                return false;
            }
            int index = c - 'A';
            counts[index]++;
            if (counts[index] > MAX_SAME_LETTER) {
                return false;
            }
        }
        return true;
    }

    private static void shuffleChars(char[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
        }
    }
}
