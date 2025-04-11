package com.sist.web;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.sist.service.ChatMessageService;
import com.sist.vo.ChatMessageVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatMessageService chatService;
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
