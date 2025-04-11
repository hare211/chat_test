package com.sist.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.dao.ChatMessageDAO;
import com.sist.service.ChatMessageService;
import com.sist.vo.ChatMessageVO;

@Controller
public class ChatController {
	
	@Autowired
	private ChatMessageService chatService;
	@GetMapping("chat/chat.do")
	public String chat() {
		return "chat/chat";
	}
	@MessageMapping("/chat")
    public void handleMessage(ChatMessageVO message) { // 실제 구현에선 Principal 에서 userId 추출
		String userId = "user"; // 테스트용 user, 실제 구현에서는 로그인 된 사용자의 Id 를 가져와야 함
        System.out.println("사용자: " + userId + " / 메시지: " + message.getContent());
        
        try {
			chatService.chatMessageInsert(message, userId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
	
}
