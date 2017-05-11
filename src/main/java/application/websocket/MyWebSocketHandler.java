package application.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.naming.AuthenticationException;

/**
 * Created by magomed on 19.04.17.
 */
public class MyWebSocketHandler extends TextWebSocketHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebSocketHandler.class);
//    private final RemotePointService remotePointService;
//
//    public MyWebSocketHandler(@NotNull RemotePointService remotePointService) {
//
//        this.remotePointService = remotePointService;
//    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
//        remotePointService.registerUser(webSocketSession.getId(), webSocketSession);

        LOGGER.debug("connection open");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws AuthenticationException {
        LOGGER.debug("");
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
       LOGGER.debug("connection close");
//       remotePointService.removeUser(webSocketSession.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
