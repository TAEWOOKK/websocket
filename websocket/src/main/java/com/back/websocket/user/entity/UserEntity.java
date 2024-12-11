package com.back.websocket.user.entity;

import com.back.websocket.room.entity.RoomEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class UserEntity {

    @Id
    private String id;

    private String email;

    private String password;

    @Indexed(unique = true)
    private String nickname;

    private String role;

    private Boolean status = false; // 디폴트 오프라인 상태

    @DBRef
    private List<RoomEntity> roomEntities;

    public void UpdateStatus(boolean status){
        this.status = status;
    }
}
