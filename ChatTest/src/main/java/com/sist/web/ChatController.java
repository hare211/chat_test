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
    public void handleMessage(ChatMessage message) { // ���� �������� Principal ���� userId ����
		String userId = "user"; // �׽�Ʈ�� user, ���� ���������� �α��� �� ������� Id �� �����;� ��
        System.out.println("�����: " + userId + " / �޽���: " + message.getContent());

        if (message.getSender() == null || message.getSender().isBlank()) {
            message.setSender(userId);
        }

        messagingTemplate.convertAndSend("/topic/chat/" + message.getRoomId(), message);
    }
	
}
