package com.sist.service;

import java.util.List;
import java.util.Map;

import com.sist.vo.ChatMessageVO;

public interface ChatMessageService {
	void chatMessageInsert(ChatMessageVO vo, String sender_id);
	List<ChatMessageVO> getLatestMessagesByRoomId(long room_id);
	void createRoom(String room_name, String user_id);
}
