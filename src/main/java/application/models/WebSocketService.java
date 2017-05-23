package application.models;

import application.websocket.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by magomed on 18.05.17.
 */
@Service
public class WebSocketService {
    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public Map<String, WebSocketSession> getSessions() {
        return sessions;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    public void setSessions(Map<String, WebSocketSession> sessions) {
        this.sessions = sessions;
    }

    public void addWebSocket(String key, WebSocketSession webSocketSession){
        sessions.put(key, webSocketSession);
    }

    public void removeSocket(String key){
        sessions.remove(key);
    }

    public WebSocketSession getSocketByKey(String key){
        return sessions.get(key);
    }

    public void sendMessageToUser(@NotNull String key, String message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(key);
        Gson gson = new Gson();

        if (webSocketSession == null) {
            throw new IOException("no game websocket for user " + key);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("session is closed or not exsists");
        }
        try {
            webSocketSession.sendMessage(new TextMessage(message));
        } catch (JsonProcessingException | WebSocketException e) {
            throw new IOException("Unnable to send message", e);
        }
    }
}
