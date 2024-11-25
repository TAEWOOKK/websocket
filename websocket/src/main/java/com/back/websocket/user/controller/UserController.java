package com.back.websocket.user.controller;

import com.back.websocket.user.dto.SignInRequestDTO;
import com.back.websocket.user.dto.SignUpRequestDTO;
import com.back.websocket.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid SignUpRequestDTO signUpRequestDTO) {

        System.out.println(signUpRequestDTO);

        return userService.signUp(signUpRequestDTO);
    }

    @GetMapping("/signIn")
    public ResponseEntity<?> signIn(SignInRequestDTO signInRequestDTO) {

        return userService.signIn(signInRequestDTO);
    }
}
