package exceptions;

public class EmptyBoxException extends Exception {
    public EmptyBoxException() { super(); }
    public EmptyBoxException(String message) { super(message); }
}
