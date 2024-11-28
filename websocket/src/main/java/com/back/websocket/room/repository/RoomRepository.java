package com.back.websocket.room.repository;

import com.back.websocket.room.entity.RoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends MongoRepository<RoomEntity,String> {
}
