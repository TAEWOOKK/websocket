package com.back.websocket.user.service;

import com.back.websocket.config.dto.StateRes;
import com.back.websocket.user.dto.CustomUserDetails;
import com.back.websocket.user.dto.SignUpRequestDTO;
import com.back.websocket.user.entity.UserEntity;
import com.back.websocket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseEntity<?> signUp(SignUpRequestDTO signUpRequestDTO){

        if(userRepository.existsByNickname(signUpRequestDTO.getNickname())){
            return new ResponseEntity<>(new StateRes(false,"중복된 닉네임 입니다."), HttpStatus.BAD_REQUEST);
        };

        try {

            UserEntity userEntity = UserEntity.builder()
                    .email(signUpRequestDTO.getEmail())
                    .password(bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword()))
                    .nickname(signUpRequestDTO.getNickname())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(userEntity);
        }catch (Exception e){
            return new ResponseEntity<>(new StateRes(false,"중복된 ID"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new StateRes(true,"회원가입 성공"), HttpStatus.OK);
    }

    public Boolean userByEmail(String email){

        return userRepository.existsByEmail(email);
    }

    public UserEntity userInfo(CustomUserDetails userDetails){

        return userRepository.findByEmail(userDetails.getUsername());
    }
}
