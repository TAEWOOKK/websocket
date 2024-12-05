package com.back.websocket.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendListDTO {

    private String id;
    private String friend_nickname;
    private Boolean friend_check;
}
