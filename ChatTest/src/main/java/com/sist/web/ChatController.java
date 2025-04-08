package com.sist.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ChatController {
	@MessageMapping("/chat")
	@SendTo("/topic/messages")
	public String handleMessage(String message) {
		System.out.println("수신된 메시지: " + message);
		return message;
	}
}
