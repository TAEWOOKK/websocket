package com.back.websocket.friend.repository;

import com.back.websocket.friend.entity.FriendEntity;
import com.back.websocket.user.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends MongoRepository<FriendEntity, String> {

    FriendEntity findByToUserAndFromUser(UserEntity to_user,UserEntity from_user);

    void deleteByToUserAndFromUser(UserEntity to_user,UserEntity from_user);

    void deleteByFromUserAndToUser(UserEntity from_user,UserEntity to_user);
}
