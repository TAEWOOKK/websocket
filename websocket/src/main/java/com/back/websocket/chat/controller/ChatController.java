package com.back.websocket.chat.controller;

import com.back.websocket.chat.dto.ChatMessageDto;
import com.back.websocket.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 채팅 메시지 처리
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageDto chatMessageDto) {
        if (chatMessageDto.getMessageType() == ChatMessageDto.MessageType.TALK) {
            // DB에 메시지 저장
            chatService.chat_save(chatMessageDto.getChatRoomId(), chatMessageDto.getEmail(), chatMessageDto.getMessage());
        }

        // 특정 채팅방의 구독 경로로 메시지 전송
        String destination = "/topic/chatRoom/" + chatMessageDto.getChatRoomId();
        messagingTemplate.convertAndSend(destination, chatMessageDto);
    }


    // 사용자에게 개인 메시지 보내기
    @MessageMapping("/chat.addUser")
    @SendToUser("/queue/reply") // 사용자에게 /user/queue/reply로 메시지를 보내기
    public ChatMessageDto addUser(ChatMessageDto chatMessageDto) {
        // 사용자가 채팅방에 들어갔을 때 처리
        log.info("채팅 유저 추가");
        chatMessageDto.setMessage(chatService.chat_List(chatMessageDto.getChatRoomId()).toString());
        return chatMessageDto;
    }
}
