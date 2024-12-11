package com.back.websocket.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")  // 모든 요청에 대해 필터를 적용합니다.
@Slf4j
public class CustomRequestLoggingFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 작업이 필요하면 여기서 처리
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 요청 정보 로그 찍기
        log.info("Request Method: {}, Request URI: {}", httpRequest.getMethod(), httpRequest.getRequestURI());

        // 필터 체인을 통해 요청을 계속 진행시킴
        chain.doFilter(request, response);
    }
}