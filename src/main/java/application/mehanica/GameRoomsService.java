package application.mehanica;

import application.models.GameSession;
import application.models.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by magomed on 17.05.17.
 */
@Service
public class GameRoomsService {
    private GameSession gameSession;
    private ObjectMapper objectMapper = new ObjectMapper();
    private WebSocketService webSocketService;
    private List<GameSession> gameSessions = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(GameRoomsService.class);


    @Autowired
    public GameRoomsService(GameSession gameSession, WebSocketService webSocketService) {
        this.gameSession = gameSession;
        this.webSocketService = webSocketService;
    }

    public boolean isGaming(@NotNull String key) {
        for (GameSession session : gameSessions) {
            if (session.getFirst().equals(key) || session.getSecond().equals(key))
                return true;
        }
        return false;
    }

    public void addGameSession(@NotNull GameSession session) {
        gameSessions.add(session);
    }

    public void updateField(GameSession gameSessionUpdate) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
        for (GameSession session : gameSessions) {
            if (session.equals(gameSessionUpdate)) {
                session.setField(gameSessionUpdate.getField());
                webSocketService.sendMessageToUser(session.getFirst(), objectMapper.writeValueAsString(session));
                webSocketService.sendMessageToUser(session.getSecond(), objectMapper.writeValueAsString(session));
                if (session.getLoginFirst().equals(gameSessionUpdate.getLoginFirst())){
                    LOGGER.info(session.getLoginFirst() + "put a cross");
                } else {
                    LOGGER.info(session.getLoginSecond() + "put a foot");
                }
                return;
            }
        }
    }

    public synchronized void updateLogin(String login, String id) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
        if (gameSession.getLoginFirst() == null || gameSession.getFirst().equals(id)){
            gameSession.setLoginFirst(login);
            LOGGER.info("create loginFirstsuccess = " + login);
        } else {
            gameSession.setLoginSecond(login);
            gameSession.setField("@@@@@@@@@");
            addGameSession(gameSession);
            LOGGER.info("create " + login + " as loginSecond and create rooms success");
            webSocketService.sendMessageToUser(id, objectMapper.writeValueAsString(gameSession));
            webSocketService.sendMessageToUser(gameSession.getFirst(), objectMapper.writeValueAsString(gameSession));
            gameSession = new GameSession();
        }
    }

    public void addUser(@NotNull String id) throws IOException {
        if (gameSession.getFirst() == null) {
            gameSession.setFirst(id);
        } else {
            gameSession.setSecond(id);
        }
    }

    public void removeRoom(GameSession sessionUpdate) throws IOException, JsonProcessingException {
        for (GameSession session : gameSessions) {
            if (session.equals(sessionUpdate)) {
                webSocketService.sendMessageToUser(session.getSecond(), objectMapper.writeValueAsString(gameSession));
                webSocketService.sendMessageToUser(session.getFirst(), objectMapper.writeValueAsString(gameSession));
                gameSessions.remove(session);
                LOGGER.info("game over");
                return;
            }
        }
    }
    public void removeRoomBySocketId(String id){
        for (GameSession session : gameSessions) {
            if (session.getFirst().equals(id) || session.getSecond().equals(id)) {
                gameSessions.remove(session);
                return;
            }
        }
    }
}
