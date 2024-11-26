package com.back.websocket.room.service;

import com.back.websocket.user.entity.QUserEntity;
import com.back.websocket.user.entity.UserEntity;
import com.back.websocket.user.repository.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
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
public class RoomService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    QUserEntity userEntity = QUserEntity.userEntity;


    public ResponseEntity<?> room_List(HttpServletRequest request){

        String email = request.getHeader("email");

        String password = request.getHeader("password");

        BooleanExpression predicate = userEntity.email.contains(email)
                .and(userEntity.email.contains(email));

        Query query = new Query();

        query.addCriteria(Criteria.where("email").regex(email));

        List<UserEntity> userEntities = mongoTemplate.find(query, UserEntity.class);

        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
