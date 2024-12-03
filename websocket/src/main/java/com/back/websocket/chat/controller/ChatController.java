package com.back.websocket.chat.controller;

import com.back.websocket.chat.dto.ChatMessageDto;
import com.back.websocket.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅 메시지 처리
    @MessageMapping("/chat.sendMessage") // 클라이언트에서 /app/chat.sendMessage 경로로 전송된 메시지를 처리
    @SendTo("/topic/public") // 메시지를 /topic/public 경로로 전송 (모든 클라이언트에게 브로드캐스트)
    public ChatMessageDto sendMessage(ChatMessageDto chatMessageDto) {
        // DB에 메시지 저장 또는 처리
        if (chatMessageDto.getMessageType() == ChatMessageDto.MessageType.TALK) {
            chatService.chat_save(chatMessageDto.getChatRoomId(), chatMessageDto.getEmail(), chatMessageDto.getMessage());
        }
        return chatMessageDto;
    }

    // 사용자에게 개인 메시지 보내기
    @MessageMapping("/chat.addUser")
    @SendToUser("/queue/reply") // 사용자에게 /user/queue/reply로 메시지를 보내기
    public ChatMessageDto addUser(ChatMessageDto chatMessageDto) {
        // 사용자가 채팅방에 들어갔을 때 처리
        chatMessageDto.setMessage(chatService.chat_List(chatMessageDto.getChatRoomId()).toString());
        return chatMessageDto;
    }
}
