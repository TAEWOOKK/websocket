package com.back.websocket.controller;

import com.back.websocket.dto.ChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        sessions.add(session);
        session.sendMessage(new TextMessage("연결완료"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        // 클라이언트로부터 받은 메세지를 ChatMessageDto로 변환
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);

        // 메세지 타입에 따라 분기
        if(chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.JOIN)){
            // 입장 메세지
            chatRoomSessionMap.computeIfAbsent(chatMessageDto.getChatRoomId(), s -> new HashSet<>()).add(session);
            chatMessageDto.setMessage(chatMessageDto.getUserName()+" 님이 입장하셨습니다.");
        }
        else if(chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.LEAVE)){
            // 퇴장 메세지
            chatRoomSessionMap.get(chatMessageDto.getChatRoomId()).remove(session);
            chatMessageDto.setMessage(chatMessageDto.getUserName()+ "님이 퇴장하셨습니다.");
        }

        // 채팅 메세지 전송
        for(WebSocketSession webSocketSession : chatRoomSessionMap.get(chatMessageDto.getChatRoomId())){
            webSocketSession.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessageDto)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        sessions.remove(session);
        log.info("연결 종료");
    }
}
