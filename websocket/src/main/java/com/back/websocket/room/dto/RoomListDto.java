package com.back.websocket.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomListDto {

    private String id;
    private String room_name;
    private int userCount;
}
