package application.models;

/**
 * Created by magomed on 18.05.17.
 */
public class Messager {
    private String login;

    public Messager(String message) {
        this.login = message;
    }

    public String getMessage() {
        return login;
    }

    public void setMessage(String message) {
        this.login = message;
    }
}
