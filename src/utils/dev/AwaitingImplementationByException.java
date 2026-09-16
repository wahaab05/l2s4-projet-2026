package utils.dev;

/**
 * Exception for methods not yet implemented
 */
public class AwaitingImplementationByException extends RuntimeException {
    
    /**
     * Class constructor
     * @param message error message
     */
    public AwaitingImplementationByException (String message)
    {
        super(message);
    }
}
