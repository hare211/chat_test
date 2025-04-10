package com.sist.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sist.service.ChatMessageService;
import com.sist.vo.ChatMessageVO;

@RestController
public class ChatRestController {
	@Autowired
	private ChatMessageService chatService;
	
	@GetMapping("/chat/{room_id}/messages")
	public ResponseEntity<List<ChatMessageVO>> getMessages(@PathVariable("room_id") int room_id) {
		List<ChatMessageVO> messages = chatService.getLatestMessagesByRoomId(room_id);
		return ResponseEntity.ok(messages);
	}
}
