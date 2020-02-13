package project.bomb.vacuum.exceptions;

/**
 * SHOULD be thrown when the state of the game does not
 * support the operation called.
 */
public class InvalidStateException extends RuntimeException {

    /**
     * {@link RuntimeException}
     */
    public InvalidStateException() {
        super("Invalid State Exception");
    }

    /**
     * {@link RuntimeException(String)}
     */
    public InvalidStateException(String message) {
        super("Invalid State Exception: " + message);
    }
}
