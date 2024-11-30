package com.back.websocket.chat.service;

import com.back.websocket.chat.dto.ChatListDTO;
import com.back.websocket.chat.entity.ChatEntity;
import com.back.websocket.chat.repository.ChatRepository;
import com.back.websocket.room.entity.RoomEntity;
import com.back.websocket.room.repository.RoomRepository;
import com.back.websocket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    
    public void chat_save(String room_id, String email ,String content){

        chatRepository.save(ChatEntity.builder()
                .content(content)
                .user(userRepository.findByEmail(email))
                .room(roomRepository.findById(room_id).orElse(null)).build());

    }

    public List<ChatListDTO> chat_List(String room_id){

        RoomEntity roomEntity = roomRepository.findById(room_id).orElse(null);

        Query query = new Query();

        query.addCriteria(Criteria.where("room.id").is(room_id));

        query.fields().include("content").include("createdAt").include("user");

        List<ChatEntity> chats = mongoTemplate.find(query, ChatEntity.class);

        return chats.stream()
                .map(chat -> new ChatListDTO(chat.getContent(),chat.getUser().getNickname(),chat.getCreatedAt()))
                .toList();
    }
}
