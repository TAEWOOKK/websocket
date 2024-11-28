package com.back.websocket.room.controller;

import com.back.websocket.room.dto.RoomInsertDTO;
import com.back.websocket.room.service.RoomService;
import com.back.websocket.user.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/Room")
@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/ListF")
    public String room_ListF(){

        return "room/roomList";
    }

    @ResponseBody
    @GetMapping("/List")
    public ResponseEntity<?> room_List(@AuthenticationPrincipal CustomUserDetails userDetails){

        return new ResponseEntity<>(roomService.room_List(userDetails), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/Insert")
    public ResponseEntity<?> room_Insert(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @Valid RoomInsertDTO dto){

        return new ResponseEntity<>(roomService.room_Insert(userDetails, dto), HttpStatus.OK);
    }
}
