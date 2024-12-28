package com.back.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StompLoggingInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // STOMP 연결 및 연결 해제 이벤트 처리
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            if (isTargetEndpoint(accessor, "/wsFriends")) {
                log.info("STOMP 연결: Session ID: {} | Endpoint: /wsFriends", accessor.getSessionId());
            }
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            if (isTargetEndpoint(accessor, "/wsFriends")) {
                log.info("STOMP 연결 종료: Session ID: {} | Endpoint: /wsFriends", accessor.getSessionId());
            }
        }

        return message;
    }

    private boolean isTargetEndpoint(StompHeaderAccessor accessor, String targetEndpoint) {
        // 연결 시엔 destination이 없을 수 있으므로, 연결된 path를 추출
        String nativeHeaderDestination = accessor.getNativeHeader("destination") != null
                ? accessor.getNativeHeader("destination").get(0)
                : null;

        // CONNECT 시에는 path 정보는 "simpSessionAttributes"에서 가져와야 함
        String connectPath = (String) accessor.getSessionAttributes().get("simpConnectPath");

        return targetEndpoint.equals(nativeHeaderDestination) || targetEndpoint.equals(connectPath);
    }
}
