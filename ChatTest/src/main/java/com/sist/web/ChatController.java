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
    public void handleMessage(ChatMessageVO message) { // ���� �������� Principal ���� userId ����
		String userId = "user"; // �׽�Ʈ�� user, ���� ���������� �α��� �� ������� Id �� �����;� ��
        System.out.println("�����: " + userId + " / �޽���: " + message.getContent());
        
        try {
			chatService.chatMessageInsert(message, userId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
	
}
