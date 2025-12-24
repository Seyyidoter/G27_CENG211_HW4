package exceptions;

/**
 * Thrown when someone tries to roll or flip a FixedBox.
 * Fixed boxes cannot move.
 */
public class UnmovableFixedBoxException extends Exception {

    private static final String DEFAULT_MESSAGE =
            "This box is fixed and cannot be moved or flipped!";

    public UnmovableFixedBoxException() {
        super(DEFAULT_MESSAGE);
    }

    public UnmovableFixedBoxException(String message) {
        super(isBlank(message) ? DEFAULT_MESSAGE : message.trim());
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
