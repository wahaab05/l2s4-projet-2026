package utils.dev;

/**
 * Exception for crashout
 */
public class INeedHelpException extends RuntimeException {
    
    /**
     * Class constructor
     * @param message error message
     */
    public INeedHelpException (String message)
    {
        super(message);
    }
}
