package com.back.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class CustomWebSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // 연결이 종료될 때 로그 남기기
        log.info("WebSocket 연결 종료: Session ID: {} | Close Status: {}", session.getId(), closeStatus);

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 연결이 수립될 때 (필요시) 로그를 남길 수 있음
        log.info("WebSocket 연결 수립: Session ID: {}", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 연결에 오류가 발생한 경우 로그를 남길 수 있음
        log.error("WebSocket 연결 오류: Session ID: {} | Error: {}", session.getId(), exception.getMessage());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
