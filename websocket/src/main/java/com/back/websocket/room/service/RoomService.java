package com.back.websocket.room.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    public ResponseEntity<?> room_List(HttpServletRequest request){

        String email = request.getHeader("email");

        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
