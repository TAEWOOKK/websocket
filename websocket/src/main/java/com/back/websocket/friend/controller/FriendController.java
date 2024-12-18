package com.back.websocket.friend.controller;

import com.back.websocket.friend.service.FriendService;
import com.back.websocket.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<?> friend_List(@AuthenticationPrincipal CustomUserDetails userDetails){

        return new ResponseEntity<>(friendService.FriendList(userDetails,true), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/plus-list")
    public ResponseEntity<?> friend_Plus_List(@AuthenticationPrincipal CustomUserDetails userDetails){

        return new ResponseEntity<>(friendService.FriendPlusList(userDetails,false), HttpStatus.OK);
    }


    @ResponseBody
    @PostMapping("/plus")
    public ResponseEntity<?> friend_Plus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestParam(value = "nickname")String nickname){

        return friendService.FriendPlus(userDetails,nickname);
    }

    @ResponseBody
    @PostMapping("/plus/{friend_id}")
    public ResponseEntity<?> friendPlusSuccess(@PathVariable("friend_id") String friend_id,@RequestParam(value = "isAccepted") Boolean isAccepted){

        if(isAccepted){
            return friendService.FriendPlusSuccess(friend_id);
        }else {
            return friendService.FriendPlusDelete(friend_id);
        }
    }

    @ResponseBody
    @DeleteMapping("/delete/{friend_id}")
    public ResponseEntity<?> deleteFriend(@PathVariable("friend_id") String friend_id,@AuthenticationPrincipal CustomUserDetails userDetails){

        return friendService.FriendDelete(friend_id,userDetails);
    }
}
