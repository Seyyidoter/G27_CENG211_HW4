package exceptions;

/**
 * Thrown when a player tries to use BoxFixer on a box
 * that is already a FixedBox.
 */
public class BoxAlreadyFixedException extends Exception {

    private static final String DEFAULT_MESSAGE =
            "This box is already fixed! Cannot apply BoxFixer.";

    public BoxAlreadyFixedException() {
        super(DEFAULT_MESSAGE);
    }

    public BoxAlreadyFixedException(String message) {
        super(isBlank(message) ? DEFAULT_MESSAGE : message.trim());
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
