package com.escapedoom.gamesession.rest.config.websocket;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Getter
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketStartedHandler webSocketStartedHandler;
    private final WebSocketYourNameHandler webSocketYourNameHandler;
    private final WebSocketAllNamesHandler webSocketAllNamesHandler;

    public WebSocketConfig(WebSocketYourNameHandler webSocketYourNameHandler, WebSocketAllNamesHandler webSocketAllNamesHandler, WebSocketStartedHandler webSocketStartedHandler) {
        this.webSocketYourNameHandler = webSocketYourNameHandler;
        this.webSocketAllNamesHandler = webSocketAllNamesHandler;
        this.webSocketStartedHandler = webSocketStartedHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketTestHandler(), "/ws/test")
                .setAllowedOrigins("*");
        registry.addHandler(webSocketAllNamesHandler, "/ws/all-names")
                .setAllowedOrigins("*");
        registry.addHandler(webSocketYourNameHandler, "/ws/your-name")
                .setAllowedOrigins("*");
        registry.addHandler(webSocketStartedHandler, "/ws/started")
                .setAllowedOrigins("*");
    }

}