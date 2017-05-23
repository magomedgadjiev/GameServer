package application.models;

/**
 * Created by magomed on 18.05.17.
 */
public class Messager {
    private String message;

    public Messager(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
