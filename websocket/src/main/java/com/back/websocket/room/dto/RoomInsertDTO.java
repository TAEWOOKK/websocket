package com.back.websocket.room.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomInsertDTO {

    @NotBlank(message = "방 제목이 공백입니다.")
    private String room_name;
    @NotBlank(message = "방 설명이 공백입니다.")
    private String description;
}
