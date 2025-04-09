package com.sist.inteceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.sist.util.JwtUtil;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    public JwtChannelInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            System.out.println("STOMP CONNECT 헤더 토큰: " + token);
            if (token != null) {
				if (token.startsWith("Bearer ")) {
					token = token.substring(7);
				}
				if (jwtUtil.validateToken(token)) {
					String userId = jwtUtil.getUserIdFromToken(token);
					accessor.setUser(new StompPrincipal(userId)); // Principal 에 userId 저장
				} else {
					throw new IllegalArgumentException("토큰 유효성 검사 실패");
				}
			} else {
				throw new IllegalArgumentException("Authoriaztion 헤더 누락");
			}
        }

        return message;
    }
}
