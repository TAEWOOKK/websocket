package com.back.websocket.dto;

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
    private Long chatRoomId;
    private String userName;
    private String message;
}
