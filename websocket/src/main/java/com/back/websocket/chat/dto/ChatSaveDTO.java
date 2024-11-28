package com.back.websocket.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatSaveDTO {

    @NotBlank(message = "방 번호가 누락되었습니다.")
    private String room_id;

    @NotBlank(message = "빈 내용입니다.")
    private String content;
}
