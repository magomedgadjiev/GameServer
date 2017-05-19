package application.websocket;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Map;

/**
 * Created by magomed on 19.04.17.
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
        LOGGER.info("connection open");
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws AuthenticationException {
        remotePointService.update(webSocketSession, message);
        LOGGER.info("send message success");
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        LOGGER.info("connection close");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}