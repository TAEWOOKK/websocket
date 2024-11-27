package com.back.websocket.user.service;

import com.back.websocket.config.dto.StateRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 로그인 실패 시 커스텀 응답
        StateRes stateRes;

        // 예외에 따라 메시지 처리 (비밀번호 오류나 아이디 없음)
        if (exception instanceof BadCredentialsException) {
            stateRes = new StateRes(false, "비밀번호가 틀렸습니다");
        } else if (exception instanceof UsernameNotFoundException) {
            stateRes = new StateRes(false, "아이디가 유효하지 않습니다");
        } else {
            stateRes = new StateRes(false, "로그인 실패");
        }

        ResponseEntity<?> responseEntity = new ResponseEntity<>(stateRes, HttpStatus.BAD_REQUEST);

        // JSON으로 응답을 반환
        response.setStatus(responseEntity.getStatusCodeValue());
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
    }
}
