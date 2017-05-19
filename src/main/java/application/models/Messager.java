package application.models;

import org.springframework.web.socket.WebSocketMessage;

/**
 * Created by magomed on 18.05.17.
 */
public class Messager implements WebSocketMessage<Messager> {
    private String message;

    public Messager(String message) {
        this.message = message;
    }

    @Override
    public Messager getPayload() {
        return new Messager("hello");
    }

    @Override
    public int getPayloadLength() {
        return 0;
    }

    @Override
    public boolean isLast() {
        return false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
