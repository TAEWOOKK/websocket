package com.back.websocket.websocket;

import com.back.websocket.chat.service.ChatService;
import com.back.websocket.websocket.dto.ChatMessageDto;
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
    private final Map<String, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();  // chatRoomId를 String으로 변경
    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        session.sendMessage(new TextMessage("연결완료"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);

        String chatRoomId = chatMessageDto.getChatRoomId();
        String email = chatMessageDto.getEmail();
        String nickName = chatMessageDto.getNickName();

        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.JOIN)) {
            // 이미 동일한 세션이 있는지 확인 후 처리
            Set<WebSocketSession> roomSessions = chatRoomSessionMap.computeIfAbsent(chatRoomId, s -> new HashSet<>());
            if (!roomSessions.add(session)) {
                log.warn("이미 세션이 존재합니다: 사용자 {}, 방 ID {}", email, chatRoomId);
                return; // 이미 세션이 존재하면 추가하지 않고 종료
            }

            chatMessageDto.setMessage(chatService.chat_List(chatRoomId).toString());
        }
        else if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.TALK)) {

            chatService.chat_save(chatRoomId, email,chatMessageDto.getMessage());

            chatMessageDto.setMessage(chatMessageDto.getMessage());
        }
        else if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.LEAVE)) {
            Set<WebSocketSession> roomSessions = chatRoomSessionMap.get(chatRoomId);
            chatMessageDto.setMessage(nickName + "님이 퇴장하셨습니다.");
            if (roomSessions != null) {
                roomSessions.remove(session);

                if (roomSessions.isEmpty()) {
                    chatRoomSessionMap.remove(chatRoomId);
                }
            }
        }

        // 유효하지 않은 세션은 제거하고 메세지 전송
        Set<WebSocketSession> validSessions = new HashSet<>();
        if (chatRoomSessionMap.containsKey(chatRoomId)) {
            for (WebSocketSession webSocketSession : chatRoomSessionMap.get(chatRoomId)) {
                if (webSocketSession.isOpen()) {
                    validSessions.add(webSocketSession);
                    webSocketSession.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessageDto)));
                } else {
                    log.warn("닫힌 세션 제거: 사용자 {}, 방 ID {}", email, chatRoomId);
                }
            }
            chatRoomSessionMap.put(chatRoomId, validSessions); // 닫힌 세션 제거 후 업데이트
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        chatRoomSessionMap.values().forEach(roomSessions -> roomSessions.remove(session)); // 모든 방에서 세션 제거
        chatRoomSessionMap.entrySet().removeIf(entry -> entry.getValue().isEmpty()); // 빈 방 제거
        log.info("연결 종료: 세션 ID {}", session.getId());
    }
}
