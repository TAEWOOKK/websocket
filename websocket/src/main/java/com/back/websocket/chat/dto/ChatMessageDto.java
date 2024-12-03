package com.back.websocket.chat.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    public enum MessageType{
        JOIN, TALK, LEAVE
    }

    private MessageType messageType;
    private String chatRoomId;
    private String email;
    private String nickName;
    private String message;
}
