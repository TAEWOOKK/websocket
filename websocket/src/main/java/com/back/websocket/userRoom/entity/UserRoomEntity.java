package com.back.websocket.userRoom.entity;

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
@Document(collection = "user_room")
public class UserRoomEntity {

    // 유저와 방의 관계를 관리하는 중간 테이블
    @Id
    private String id;

    @DBRef
    private UserEntity user;

    @DBRef
    private RoomEntity room;
}
