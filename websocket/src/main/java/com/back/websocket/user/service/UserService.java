package com.back.websocket.user.service;

import com.back.websocket.config.dto.StateRes;
import com.back.websocket.user.dto.SignInRequestDTO;
import com.back.websocket.user.dto.SignUpRequestDTO;
import com.back.websocket.user.entity.UserEntity;
import com.back.websocket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<?> signUp(SignUpRequestDTO signUpRequestDTO){

        try {

            UserEntity userEntity = UserEntity.builder()
                    .email(signUpRequestDTO.getEmail())
                    .password(signUpRequestDTO.getPassword())
                    .build();

            userRepository.save(userEntity);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new StateRes(false,"중복된 ID"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new StateRes(true,"회원가입 성공"), HttpStatus.OK);
    }

    public ResponseEntity<?> signIn(SignInRequestDTO signInRequestDTO){

        if(!userRepository.existsByEmail(signInRequestDTO.getEmail())){

            return new ResponseEntity<>(new StateRes(false,"아이디가 유효하지 않습니다"),HttpStatus.BAD_REQUEST);
        }else{
            UserEntity byEmail = userRepository.findByEmail(signInRequestDTO.getEmail());

            if(byEmail.getPassword().equals(signInRequestDTO.getPassword())){

                return new ResponseEntity<>(new StateRes(true,"로그인 성공"), HttpStatus.OK);
            }else{

                return new ResponseEntity<>(new StateRes(false,"비밀번호가 틀렸습니다"), HttpStatus.BAD_REQUEST);
            }
        }
    }
}
