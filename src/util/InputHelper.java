package util;

import grid.Position;
import java.util.Scanner;

/**
 * Utility class for handling user inputs.
 * Uses Position.parse() to maintain consistency with the grid package.
 */
public class InputHelper {
    
    // Single scanner instance for the application
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads a line of text from the user.
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads an integer within the specified range (inclusive).
     * Used for menu selections (e.g., [1] Yes or [2] No).
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
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
            String input = scanner.nextLine().trim();
            try {
                //
                return Position.parse(input);
            } catch (IllegalArgumentException e) {
                System.out.println("INCORRECT INPUT: Please enter a valid location (e.g., R1-C1).");
            }
        }
    }
}
