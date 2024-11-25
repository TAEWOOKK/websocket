package com.back.websocket.exception;

import com.back.websocket.config.dto.StateRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    // MethodArgumentNotValidException을 처리하여 검증 오류를 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StateRes> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // 첫 번째 필드 오류를 가져오기
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        // StateRes 객체 생성하여 오류 메시지 반환
        StateRes stateRes = new StateRes(false, errorMessage);
        return ResponseEntity.badRequest().body(stateRes);
    }
}
