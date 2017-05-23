package application.websocket;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.naming.AuthenticationException;
import java.io.IOException;

/**
 * Created by magomed on 19.05.17.
 */
public class MyWebSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = Logger.getLogger(MyWebSocketHandler.class);
    private RemotePointService remotePointService;

    @Autowired
    public MyWebSocketHandler(RemotePointService remotePointService1) {
        this.remotePointService = remotePointService1;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException, IOException {
        try {
            remotePointService.registerUser(webSocketSession);
            LOGGER.info("connection open");
        } catch (RuntimeException e){
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws AuthenticationException, IOException, com.fasterxml.jackson.core.JsonParseException, com.fasterxml.jackson.databind.JsonMappingException {
        remotePointService.update(webSocketSession, message);
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        final String id = webSocketSession.getId();
        remotePointService.removeUser(id);

        LOGGER.info("connection close");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
