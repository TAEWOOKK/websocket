package com.back.websocket.friend.controller;

import com.back.websocket.friend.dto.FriendListDTO;
import com.back.websocket.friend.dto.FriendStatusDTO;
import com.back.websocket.friend.service.FriendService;
import com.back.websocket.user.entity.UserEntity;
import com.back.websocket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

        UserEntity userEntity = userRepository.findByEmail(principal.getName());

        userEntity.UpdateStatus(true);
        userRepository.save(userEntity);

        List<FriendListDTO> friends = friendService.FriendList(principal.getName(), true);

        friends.forEach(friend -> {
            messagingTemplate.convertAndSendToUser(
                    friend.getEmail(),
                    "/friendsSocket/reply",
                    new FriendStatusDTO(userEntity.getId(), true) // 친구의 상태를 DTO로 전달
            );
        });
    }

    @MessageMapping("/friend.offline")
    public void friendOffline(Principal principal) {

        log.info("요청들어옴");

        UserEntity userEntity = userRepository.findByEmail(principal.getName());

        // 상태 업데이트 (오프라인으로)
        userEntity.UpdateStatus(false);
        userRepository.save(userEntity);

        // 친구 목록 가져오기
        List<FriendListDTO> friends = friendService.FriendList(principal.getName(), true);

        // 친구들에게 사용자 상태를 알림
        friends.forEach(friend -> {
            messagingTemplate.convertAndSendToUser(
                    friend.getEmail(),
                    "/friendsSocket/reply",
                    new FriendStatusDTO(userEntity.getId(), false)
            );
        });

        log.info("사용자 상태가 오프라인으로 업데이트됨: {}", userEntity.getNickname());
    }
}
