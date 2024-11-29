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
    
    public void chat_save(String room_id, String email ,String content){
        
        chatRepository.save(ChatEntity.builder()
                .content(content)
                .user(userRepository.findByEmail(email))
                .room(roomRepository.findById(room_id).orElse(null)).build());

    }
}
