package com.back.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

import java.io.IOException;

@Configuration
@Slf4j
@EnableWebSocketMessageBroker // STOMP를 사용하기 위해 필요한 어노테이션
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        registry.addEndpoint("/wsFriends")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // 메시지 브로커 경로
        registry.setApplicationDestinationPrefixes("/app"); // 메시지 전송 경로

        registry.enableSimpleBroker("/friendsSocket"); // 친구 실시간 리스트 브로커
        registry.setApplicationDestinationPrefixes("/friends"); // 친구 실시간 리스트 전송 경로
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        // Custom WebSocketHandlerDecorator를 설정하여 연결 종료 시 로그 찍기
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        super.afterConnectionClosed(session, closeStatus);
                        // 연결 종료 후 로그 출력
                        log.info("WebSocket 연결 종료: " + session.getId() + " 상태: " + closeStatus);
                    }
                };
            }
        });
    }
}
