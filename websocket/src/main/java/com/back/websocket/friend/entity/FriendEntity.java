package com.back.websocket.friend.entity;

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
@Document(collection = "friend")
public class FriendEntity {

    @Id
    private String id;

    //요청을 보낸 유저
    @DBRef
    private UserEntity fromUser;

    //요청을 받은 유저
    @DBRef
    private UserEntity toUser;

    private boolean friend_check;

    public void UpdateFriend_check(boolean friend_check){
        this.friend_check = friend_check;
    }
}
