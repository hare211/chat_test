package com.sist.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.sist.vo.ChatMessage;

@Controller
public class ChatController {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/chat")
    public void handleMessage(ChatMessage message) { // 실제 구현에선 Principal 에서 userId 추출
		String userId = "user"; // 테스트용 user, 실제 구현에서는 로그인 된 사용자의 Id 를 가져와야 함
        System.out.println("사용자: " + userId + " / 메시지: " + message.getContent());

        if (message.getSender() == null || message.getSender().isBlank()) {
            message.setSender(userId);
        }

        messagingTemplate.convertAndSend("/topic/chat/" + message.getRoomId(), message);
    }
	
}
