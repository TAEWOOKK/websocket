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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public List<FriendListDTO> FriendList(String email,Boolean friend_check){

        UserEntity byEmail = userRepository.findByEmail(email);

        Query query = new Query();

        query.addCriteria(Criteria
                .where("fromUser.id").is(byEmail.getId())
                .and("friend_check").is(friend_check));

        query.fields().include("toUser").include("friend_check");

        List<FriendEntity> friends = mongoTemplate.find(query, FriendEntity.class);

        return friends.stream()
                .map(friend -> new FriendListDTO(
                        friend.getToUser().getId(),
                        friend.getToUser().getEmail(),
                        friend.getToUser().getNickname(),
                        friend.getToUser().getStatus(),
                        friend.isFriend_check()
                ))
                .toList();
    }

    public List<FriendListDTO> FriendPlusList(CustomUserDetails userDetails,Boolean friend_check){

        UserEntity byEmail = userRepository.findByEmail(userDetails.getUsername());

        Query query = new Query();

        query.addCriteria(Criteria
                .where("toUser.id").is(byEmail.getId())
                .and("friend_check").is(friend_check));

        query.fields().include("fromUser").include("friend_check");

        List<FriendEntity> friends = mongoTemplate.find(query, FriendEntity.class);

        return friends.stream()
                .map(friend -> new FriendListDTO(
                        friend.getId(),
                        friend.getFromUser().getEmail(),
                        friend.getFromUser().getNickname(),
                        friend.getFromUser().getStatus(),
                        friend.isFriend_check()
                ))
                .toList();
    }

    public ResponseEntity<?> FriendPlus(CustomUserDetails userDetails,String nickname){

        UserEntity FromUser = userRepository.findByEmail(userDetails.getUsername());

        UserEntity ToUser = userRepository.findByNickname(nickname);

        if(ToUser == null){
            return new ResponseEntity<>(new StateRes(false,"유저를 찾을 수 없습니다."), HttpStatus.BAD_REQUEST);
        }

        FriendEntity friendCheck = friendRepository.findByToUserAndFromUser(ToUser,FromUser);

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

    @Transactional
    public ResponseEntity<?> FriendPlusSuccess(String friend_id){


        friendRepository.findById(friend_id).ifPresent(byId -> {
            byId.UpdateFriend_check(true);
            friendRepository.save(byId);

            friendRepository.save(FriendEntity.builder()
                            .fromUser(byId.getToUser())
                            .toUser(byId.getFromUser())
                            .friend_check(true)
                    .build());
        });

        return new ResponseEntity<>(new StateRes(true,"수락하였습니다."), HttpStatus.OK);
    }

    public ResponseEntity<?> FriendPlusDelete(String friend_id){

        friendRepository.deleteById(friend_id);

        return new ResponseEntity<>(new StateRes(true,"거절하였습니다."), HttpStatus.OK);
    }

    public ResponseEntity<?> FriendDelete(String friend_id, UserDetails userDetails){

        UserEntity byEmail = userRepository.findByEmail(userDetails.getUsername());

        UserEntity byId = userRepository.findById(friend_id).orElse(null);

        friendRepository.deleteByFromUserAndToUser(byEmail,byId);
        friendRepository.deleteByToUserAndFromUser(byEmail,byId);

        return new ResponseEntity<>(new StateRes(true,"삭제하였습니다."), HttpStatus.OK);
    }
}
