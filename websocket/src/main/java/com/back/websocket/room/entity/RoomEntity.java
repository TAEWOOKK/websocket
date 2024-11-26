package com.back.websocket.room.entity;

import com.back.websocket.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "room")
@Entity
public class RoomEntity {

    @Id
    private String id;

    private String room_name;

    private String description;

    @DBRef
    @OneToMany
    private List<UserEntity> userEntities;
}
