package com.back.websocket.chat.service;

import com.back.websocket.chat.dto.ChatSaveDTO;
import com.back.websocket.chat.entity.ChatEntity;
import com.back.websocket.chat.repository.ChatRepository;
import com.back.websocket.config.dto.StateRes;
import com.back.websocket.room.repository.RoomRepository;
import com.back.websocket.user.dto.CustomUserDetails;
import com.back.websocket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    
    public StateRes chat_save(ChatSaveDTO dto, CustomUserDetails userDetails){
        
        chatRepository.save(ChatEntity.builder()
                .content(dto.getContent())
                .user(userRepository.findByEmail(userDetails.getUsername()))
                .room(roomRepository.findById(dto.getRoom_id()).orElse(null)).build());
        
        return new StateRes(true,"채팅 등록 성공");
    }
}
