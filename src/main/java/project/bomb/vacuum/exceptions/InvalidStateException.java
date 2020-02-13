package project.bomb.vacuum.exceptions;

public class InvalidStateException extends RuntimeException {

    public InvalidStateException() {
        super("Invalid State Exception");
    }

    public InvalidStateException(String message) {
        super("Invalid State Exception: " + message);
    }
}
