package com.back.websocket.user.service;

import com.back.websocket.config.dto.StateRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;

@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ApplicationContext applicationContext;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{
        // 로그인 실패 시 커스텀 응답
        StateRes stateRes;

        UserService userService = applicationContext.getBean(UserService.class);
        Boolean exist = userService.userByEmail(request.getParameter("email"));

        // 예외에 따라 메시지 처리 (비밀번호 오류나 아이디 없음)
        if(!exist){
            stateRes = new StateRes(false, "ID invalid");
        }else{
            stateRes = new StateRes(false, "password wrong");
        }

        ResponseEntity<?> responseEntity = new ResponseEntity<>(stateRes, HttpStatus.BAD_REQUEST);

        // JSON으로 응답을 반환
        response.setStatus(responseEntity.getStatusCode().value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
    }
}
