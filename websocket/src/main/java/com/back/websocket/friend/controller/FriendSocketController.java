package com.back.websocket.friend.controller;

import com.back.websocket.friend.dto.FriendListDTO;
import com.back.websocket.friend.dto.FriendStatusDTO;
import com.back.websocket.friend.service.FriendService;
import com.back.websocket.user.dto.CustomUserDetails;
import com.back.websocket.user.entity.UserEntity;
import com.back.websocket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FriendSocketController {

    private final UserRepository userRepository;
    private final FriendService friendService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/friend.online")
    public void friendOnline(Principal principal){

        System.out.println("연결됨");

        System.out.println(principal.getName());

        UserEntity userEntity = userRepository.findByEmail(principal.getName());

        userEntity.UpdateStatus(true);
        userRepository.save(userEntity);

        List<FriendListDTO> friends = friendService.FriendList((CustomUserDetails) principal, true);

        friends.forEach(friend -> {
            messagingTemplate.convertAndSendToUser(
                    friend.getId(),
                    "/friendsSocket/reply",
                    new FriendStatusDTO(userEntity.getId(), true) // 친구의 상태를 DTO로 전달
            );
        });
    }

    @MessageMapping("/friend.offline")
    public void friendOffline(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername());

        // 상태 업데이트 (오프라인으로)
        userEntity.UpdateStatus(false);
        userRepository.save(userEntity);

        // 친구 목록 가져오기
        List<FriendListDTO> friends = friendService.FriendList(userDetails, true);

        // 친구들에게 사용자 상태를 알림
        friends.forEach(friend -> {
            messagingTemplate.convertAndSendToUser(
                    friend.getId(),
                    "/queue/status",
                    new FriendStatusDTO(userEntity.getId(), false)
            );
        });

        log.info("사용자 상태가 오프라인으로 업데이트됨: {}", userEntity.getNickname());
    }
}
