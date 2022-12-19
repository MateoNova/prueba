package cat.tecnocampus.fcgexam21.application.exception;

public class UserDoesNotExistsException extends RuntimeException {

    public String username;

    public UserDoesNotExistsException(String message) {
        super(message);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
