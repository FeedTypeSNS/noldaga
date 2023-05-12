package com.noldaga.configuration;

import com.noldaga.controller.handler.ChatRoomSocketHandler;
import com.noldaga.controller.handler.ChattingSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
    @Autowired
    ChattingSocketHandler chattingSocketHandler;

    @Autowired
    ChatRoomSocketHandler chatRoomSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chattingSocketHandler, "/chatting/{uuid}/{name}");
        registry.addHandler(chatRoomSocketHandler, "/chatroom/{name}");
    }
}
