package rage.exceptions;

public class AuthenticationFailedException extends Exception {

    public AuthenticationFailedException() {
        super();
    }

    public AuthenticationFailedException(final String message) {
        super(message);
    }

    public AuthenticationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AuthenticationFailedException(final Throwable cause) {
        super(cause);
    }


}
