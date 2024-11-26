package com.back.websocket.room.entity;

import com.back.websocket.userRoom.entity.UserRoomEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "room")
public class RoomEntity {

    @Id
    private String id;

    private String room_name;

    private String description;

    @DBRef
    private List<UserRoomEntity> userRoomEntities;
}
