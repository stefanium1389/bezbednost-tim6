package bezbednosttim6.exception;

public class UserNotFoundException extends ObjectNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}