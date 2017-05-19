package application.mehanica;

import application.models.GameSession;
import application.models.Messager;
import application.models.WebSocketService;
import application.websocket.Message;
import application.websocket.MyWebSocketHandler;
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
    private WebSocketService webSocketService;
    private List<GameSession> gameSessions = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(MyWebSocketHandler.class);


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

    public void updateField(GameSession gameSessionUpdate) {
        for (GameSession gameSession : gameSessions) {
            if (gameSession.equals(gameSessionUpdate)) {
                gameSession.setField(gameSessionUpdate.getField());
            }
        }
    }

    public synchronized void updateLogin(String login, String id) throws IOException {
        final Gson gson = new Gson();
        if (gameSession.getLoginFirst() == null){
            gameSession.setLoginFirst(login);
            LOGGER.info("create loginFirstsuccess");
        } else {
            gameSession.setLoginSecond(login);
            addGameSession(gameSession);
            LOGGER.info("create loginFirst and create rooms success");
            webSocketService.sendMessageToUser(id, gson.toJson(gameSession));
            gameSession = new GameSession();
        }
    }

    public void addUser(@NotNull String id) throws IOException {
        Gson gson = new Gson();
        if (gameSession.getFirst() == null) {
            gameSession.setFirst(id);
        } else {
            gameSession.setSecond(id);
        }
    }

    public void removeUser(GameSession sessionUpdate) {
        for (GameSession session : gameSessions) {
            if (session.equals(sessionUpdate)) {
                gameSessions.remove(session);
            }
        }

    }
}
