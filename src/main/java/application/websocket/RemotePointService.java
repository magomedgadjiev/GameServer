package application.websocket;

import application.mehanica.GameRoomsService;
import application.models.GameSession;
import application.models.WebSocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class RemotePointService {
    private GameRoomsService gameRoomsService;
    private WebSocketService webSocketService;
    private static final Logger LOGGER = Logger.getLogger(RemotePointService.class);
    private ObjectMapper objectMapper = new ObjectMapper();


    public RemotePointService(GameRoomsService gameRoomsService, WebSocketService webSocketService) {
        this.gameRoomsService = gameRoomsService;
        this.webSocketService = webSocketService;
    }

    public void registerUser(String key, @NotNull WebSocketSession webSocketSession) throws IOException {
        webSocketService.addWebSocket(key, webSocketSession);
        gameRoomsService.addUser(webSocketSession.getId());
        LOGGER.info("addUser success");
    }

    public void update(@NotNull WebSocketSession webSocketSession, @NotNull TextMessage textMessage) {
        final Gson gson = new Gson();
        final GameSession gameSession = gson.fromJson(textMessage.getPayload(), GameSession.class);
        if (gameSession.isEnd()) {
            removeUser(gameSession.getFirst());
            gameRoomsService.removeUser(gameSession);
        } else {
            gameRoomsService.updateField(gameSession);
        }
        LOGGER.info("update success");
    }

    public synchronized void setLogin(String key, @NotNull WebSocketSession webSocketSession, @NotNull TextMessage textMessage) throws IOException, com.fasterxml.jackson.core.JsonParseException, com.fasterxml.jackson.databind.JsonMappingException {
        final Gson gson = new Gson();
        final Message message = gson.fromJson(textMessage.getPayload(), Message.class);
        gameRoomsService.updateLogin(message.getContent(), webSocketSession.getId());
        LOGGER.info("update success");
    }

    public void removeUser(String key) {
        webSocketService.removeSocket(key);
    }

    public boolean isRegister(String key) {
        return webSocketService.getSocketByKey(key) == null;
    }

}
