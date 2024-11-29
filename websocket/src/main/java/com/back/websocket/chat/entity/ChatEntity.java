package com.back.websocket.chat.entity;

import com.back.websocket.config.entity.BaseEntity;
import com.back.websocket.room.entity.RoomEntity;
import com.back.websocket.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat")
public class ChatEntity extends BaseEntity {

    @Id
    private String id;

    private String content;

    @DBRef
    private UserEntity user;

    @DBRef
    private RoomEntity room;
}
