package project.bomb.vacuum.exceptions;

public class InvalidBoardConfiguration extends RuntimeException {

    public InvalidBoardConfiguration() {
        super("Invalid Board Configuration");
    }

    public InvalidBoardConfiguration(String message) {
        super("Invalid Board Configuration: " + message);
    }
}
