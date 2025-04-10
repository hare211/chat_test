package com.sist.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.dao.ChatMessageDAO;
import com.sist.vo.ChatMessageVO;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatMessageDAO cDao;
	
	@Transactional
	@Override
	public void chatMessageInsert(ChatMessageVO message, String sender_id) {
		if (message.getSender_id() == null || message.getSender_id().isBlank()) {
			message.setSender_id(sender_id);
		}
		// DB 에 메시지 Insert
		cDao.chatMessageInsert(message);
		// stomp 전송 실패 시 전체 rollback
		try {
			// convertAndSend(destination, message)
			messagingTemplate.convertAndSend("/topic/chat/" + message.getRoom_id(), message);
		} catch (Exception ex) {
			throw new RuntimeException("STOMP 전송 실패", ex);
		}
		
	}

	@Override
	public List<ChatMessageVO> getLatestMessagesByRoomId(int room_id) {
		return cDao.getLatestMessagesByRoomId(room_id);
	}

	@Override
	public void createRoom(String room_name) {
		cDao.createRoom(room_name);
	}

}
