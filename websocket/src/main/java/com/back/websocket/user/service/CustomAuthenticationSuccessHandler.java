package com.back.websocket.user.service;

import com.back.websocket.config.dto.StateRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 시 커스텀 응답
        ResponseEntity<?> responseEntity = new ResponseEntity<>(new StateRes(true, "로그인 성공"), HttpStatus.OK);

        // JSON으로 응답을 반환
        response.setStatus(responseEntity.getStatusCodeValue());
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
    }
}
