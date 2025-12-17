package util;

import grid.Position;

import java.util.Scanner;

/**
 * Utility class for handling user inputs.
 * Uses Position.parse() to maintain consistency with the grid package.
 */
public final class InputHelper {

    // Single scanner instance for the application
    private static final Scanner SCANNER = new Scanner(System.in);

    private InputHelper() {
        // utility class; prevent instantiation
    }

    /**
     * Reads a non-empty line of text from the user.
     */
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine();
            if (line != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    return trimmed;
                }
            }
            System.out.println("INCORRECT INPUT: Please enter a non-empty input.");
        }
    }

    /**
     * Reads an integer within the specified range (inclusive).
     * Used for menu selections (e.g., [1] Yes or [2] No).
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine();
            if (input == null) {
                System.out.println("INCORRECT INPUT: Please enter a valid number.");
                continue;
            }
            input = input.trim();

            if (input.isEmpty()) {
                System.out.println("INCORRECT INPUT: Please enter a valid number.");
                continue;
            }

            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("INCORRECT INPUT: Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("INCORRECT INPUT: Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a Position object from the user.
     * Relies on Position.parse() for validation.
     */
    public static Position readPosition(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine();
            if (input == null) {
                System.out.println("INCORRECT INPUT: Please enter a valid location (e.g., R1-C1).");
                continue;
            }
            input = input.trim();

            if (input.isEmpty()) {
                System.out.println("INCORRECT INPUT: Please enter a valid location (e.g., R1-C1).");
                continue;
            }

            try {
                return Position.parse(input);
            } catch (IllegalArgumentException e) {
                System.out.println("INCORRECT INPUT: Please enter a valid location (e.g., R1-C1).");
            }
        }
    }
}
