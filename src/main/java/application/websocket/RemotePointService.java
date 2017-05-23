package application.websocket;

import application.mehanica.GameRoomsService;
import application.models.GameSession;
import application.models.WebSocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.LinkedHashMap;

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

    public void registerUser(@NotNull WebSocketSession webSocketSession) throws IOException {
        webSocketService.addWebSocket(webSocketSession.getId(), webSocketSession);
        gameRoomsService.addUser(webSocketSession.getId());
        LOGGER.info("addUser success");
    }

    public synchronized void update(@NotNull WebSocketSession webSocketSession, @NotNull TextMessage textMessage) throws IOException, com.fasterxml.jackson.core.JsonParseException, com.fasterxml.jackson.databind.JsonMappingException {
        try {
            final Message message = objectMapper.readValue(textMessage.getPayload(), Message.class);
            switch (message.getType()) {
                case 1: {
                    setLogin(message.getContent(), webSocketSession.getId());
                    LOGGER.info("update success");
                    break;
                }
                case 2: {
                    updateRoomService(message.getContent());
                    LOGGER.info("update success");
                }
                default: break;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "error");
        }
    }

    public void updateRoomService(@NotNull Object object) throws IOException {
        final LinkedHashMap linkedHashMap = (LinkedHashMap) object;
        final GameSession gameSession = new GameSession((String) linkedHashMap.get("loginFirst"), (String) linkedHashMap.get("loginSecond"), (String) linkedHashMap.get("field"));
        if (gameSession.isEnd()) {
            gameRoomsService.removeRoom(gameSession);
            LOGGER.info("game over");
        } else {
            gameRoomsService.updateField(gameSession);
        }
    }

    public void setLogin(@NotNull Object login, String id) throws IOException {
        gameRoomsService.updateLogin((String) login, id);
        LOGGER.info("set" + login + "success");
    }

    public void removeUser(String key) {
        gameRoomsService.removeRoomBySocketId(key);
        webSocketService.removeSocket(key);
    }
}
