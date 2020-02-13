package project.bomb.vacuum.exceptions;

/**
 * 
 * @author rylee.wilson
 */
public class InvalidStateException extends RuntimeException {

    /**
     * 
     */
    public InvalidStateException() {
        super("Invalid State Exception");
    }

    /**
     * 
     * @param message 
     */
    public InvalidStateException(String message) {
        super("Invalid State Exception: " + message);
    }
}
