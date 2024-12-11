package com.back.websocket.user.service;

import com.back.websocket.config.dto.StateRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        
        log.info("로그인 성공");
        // 로그인 성공 시 커스텀 응답
        ResponseEntity<?> responseEntity = new ResponseEntity<>(new StateRes(true, "login success"), HttpStatus.OK);

        // JSON으로 응답을 반환
        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
    }
}
