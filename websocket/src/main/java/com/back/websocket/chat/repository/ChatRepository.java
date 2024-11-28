package com.back.websocket.chat.repository;

import com.back.websocket.chat.entity.ChatEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends MongoRepository<ChatEntity,String> {
}
