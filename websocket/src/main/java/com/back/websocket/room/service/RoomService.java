package com.back.websocket.room.service;

import com.back.websocket.config.dto.StateRes;
import com.back.websocket.room.dto.RoomInsertDTO;
import com.back.websocket.room.dto.RoomListDto;
import com.back.websocket.room.entity.RoomEntity;
import com.back.websocket.room.repository.RoomRepository;
import com.back.websocket.user.dto.CustomUserDetails;
import com.back.websocket.user.entity.UserEntity;
import com.back.websocket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public List<RoomListDto> room_List(CustomUserDetails userDetails){

        String email = userDetails.getUsername();
        UserEntity byEmail = userRepository.findByEmail(email);

        Query query = new Query();

        query.addCriteria(Criteria.where("userEntities.id").is(byEmail.getId()));
        query.fields().include("id").include("room_name").include("userEntities");

        List<RoomEntity> rooms = mongoTemplate.find(query, RoomEntity.class);

        return rooms.stream()
                .map(room -> new RoomListDto(
                        room.getId(),
                        room.getRoom_name(),
                        room.getUserEntities() != null ? room.getUserEntities().size() : 0 // userEntities 크기 포함
                ))
                .toList();
    }

    public StateRes room_Insert(CustomUserDetails userDetails, RoomInsertDTO insertDTO){

        String email = userDetails.getUsername();

        UserEntity byEmail = userRepository.findByEmail(email);

        UserEntity randomUser = new UserEntity();

        while (true){

            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.sample(1)  // sample(1)은 1개의 무작위 문서를 반환
            );
            AggregationResults<UserEntity> result = mongoTemplate.aggregate(aggregation, UserEntity.class, UserEntity.class);

            randomUser = result.getUniqueMappedResult();

            if(randomUser != null && !randomUser.getId().equals(byEmail.getId())){
                break;
            }
        }

        List<UserEntity> userEntities = new ArrayList<>(Arrays.asList(randomUser, byEmail));



        roomRepository.save(RoomEntity.builder()
                .room_name(insertDTO.getRoom_name())
                .description(insertDTO.getDescription())
                .userEntities(userEntities)
                .build());

        return new StateRes(true,"채팅방이 개설되었습니다.");
    }
}
