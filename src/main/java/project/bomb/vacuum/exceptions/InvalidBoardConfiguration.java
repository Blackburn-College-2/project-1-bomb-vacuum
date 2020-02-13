package project.bomb.vacuum.exceptions;

/**
 * 
 * @author rylee.wilson
 */
public class InvalidBoardConfiguration extends RuntimeException {

    /**
     * 
     */
    public InvalidBoardConfiguration() {
        super("Invalid Board Configuration");
    }

    /**
     * 
     * @param message 
     */
    public InvalidBoardConfiguration(String message) {
        super("Invalid Board Configuration: " + message);
    }
}
