package exceptions;

/**
 * Thrown when a player opens a box but there is no tool inside.
 * The turn is wasted.
 */
public class EmptyBoxException extends Exception {

    private static final String DEFAULT_MESSAGE =
            "The box is empty! No tool acquired.";

    public EmptyBoxException() {
        super(DEFAULT_MESSAGE);
    }

    public EmptyBoxException(String message) {
        super(isBlank(message) ? DEFAULT_MESSAGE : message.trim());
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
