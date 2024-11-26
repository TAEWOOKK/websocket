package com.back.websocket.user.entity;

import com.back.websocket.room.entity.RoomEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
@Entity
public class UserEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @DBRef
    @OneToMany
    private List<RoomEntity> roomEntities;
}
