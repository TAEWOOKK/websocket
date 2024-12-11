package com.back.websocket.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendStatusDTO {
    private String userId;    // 상태 변경된 사용자 ID
    private boolean status;   // true: 온라인, false: 오프라인
}
