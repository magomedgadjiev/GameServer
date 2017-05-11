package application.websocket;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Solovyev on 02/11/2016.
 */
@Service
public class RemotePointService {
    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void registerUser(@NotNull String userId, @NotNull WebSocketSession webSocketSession) {
        sessions.put(userId, webSocketSession);
    }



    public boolean isConnected(@NotNull String userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    public void removeUser(@NotNull String userId)
    {
        sessions.remove(userId);
    }

    public void cutDownConnection(@NotNull String userId, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
            }
        }
    }
}
