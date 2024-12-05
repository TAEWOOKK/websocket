package com.back.websocket.friend.service;

import com.back.websocket.config.dto.StateRes;
import com.back.websocket.friend.dto.FriendListDTO;
import com.back.websocket.friend.entity.FriendEntity;
import com.back.websocket.friend.repository.FriendRepository;
import com.back.websocket.user.dto.CustomUserDetails;
import com.back.websocket.user.entity.UserEntity;
import com.back.websocket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public List<FriendListDTO> friend_List(CustomUserDetails userDetails){

        //List<FriendEntity> byToUser = friendRepository.findByTo_user(userRepository.findByEmail(userDetails.getUsername()));

        Query query = new Query();

        query.addCriteria(Criteria
                .where("friend.fromUser.id").is(userDetails.getUsername())
                .and("friend.friend_check").is(true));

        query.fields().include("toUser.id").include("toUser.nickname").include("friend_check");

        List<FriendEntity> friends = mongoTemplate.find(query, FriendEntity.class);

        return friends.stream()
                .map(friend -> new FriendListDTO(
                        friend.getToUser().getId(),
                        friend.getToUser().getNickname(),
                        friend.isFriend_check()))
                .toList();
    }

    public ResponseEntity<?> friend_Plus(CustomUserDetails userDetails,String nickname){

        UserEntity FromUser = userRepository.findByEmail(userDetails.getUsername());

        UserEntity ToUser = userRepository.findByNickname(nickname);

        if(ToUser == null){
            return new ResponseEntity<>(new StateRes(false,"유저를 찾을 수 없습니다."), HttpStatus.BAD_REQUEST);
        }

        FriendEntity friendCheck = friendRepository.findByToUserAndFromUser(FromUser,ToUser);

        if(friendCheck != null){
            if(friendCheck.isFriend_check()){
                return new ResponseEntity<>(new StateRes(false,"이미 친구입니다."), HttpStatus.BAD_REQUEST);
            }else{
                return new ResponseEntity<>(new StateRes(false,"이미 요청한 친구입니다."), HttpStatus.BAD_REQUEST);
            }
        }

        friendRepository.save(
                FriendEntity.builder()
                        .fromUser(FromUser)
                        .toUser(ToUser)
                        .friend_check(false)
                .build());

        return new ResponseEntity<>(new StateRes(true,"요청을 보냈습니다."), HttpStatus.OK);
    }
}
