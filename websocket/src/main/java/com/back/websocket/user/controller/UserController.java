package com.back.websocket.user.controller;

import com.back.websocket.user.dto.CustomUserDetails;
import com.back.websocket.user.dto.SignInRequestDTO;
import com.back.websocket.user.dto.SignUpRequestDTO;
import com.back.websocket.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/sign-up")
    public String signUpF(){

        return "/login/signup";
    }

    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid SignUpRequestDTO signUpRequestDTO) {

        return userService.signUp(signUpRequestDTO);
    }

    @GetMapping("/login")
    public String loginF(){

        return "/login/login";
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> userInfo(@AuthenticationPrincipal CustomUserDetails userDetails){

        return new ResponseEntity<>(userService.userInfo(userDetails), HttpStatus.OK);
    }
}
