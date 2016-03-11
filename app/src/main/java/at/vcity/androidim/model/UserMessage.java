package at.vcity.androidim.model;

/**
 * Created by akbank on 27/11/15.
 */
public class UserMessage {

    public String username;
    public String message;

    public UserMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public UserMessage() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
