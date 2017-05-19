package application;

import application.websocket.MyWebSocketHandler;
import application.websocket.RegisterWebSocketHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * Created by Solovyev on 06/11/2016.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final @NotNull WebSocketHandler webSocketHandler1;
    private final @NotNull WebSocketHandler webSocketHandler2;

    public WebSocketConfig(@NotNull @Qualifier("gameWebSocketHandler") WebSocketHandler webSocketHandler1, @NotNull @Qualifier("getRegisterWebSocketHandler") WebSocketHandler webSocketHandler2) {
        this.webSocketHandler1 =  webSocketHandler1;
        this.webSocketHandler2 =  webSocketHandler2;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler1, "/game")
                .addHandler(webSocketHandler2, "/register")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}
