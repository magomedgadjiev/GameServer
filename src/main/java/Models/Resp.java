package Models;

/**
 * Created by magomed on 17.03.17.
 */
public class Resp {
    private int key;

    private String message;

    public Resp(int key, String message) {
        this.key = key;
        this.message = message;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
