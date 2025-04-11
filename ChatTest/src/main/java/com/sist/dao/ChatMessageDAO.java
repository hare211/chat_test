package com.sist.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sist.mapper.ChatMessageMapper;
import com.sist.vo.ChatMessageVO;

@Repository
public class ChatMessageDAO {
	@Autowired
	private ChatMessageMapper mapper;
	/** �޽��� ���� */
	public void chatMessageInsert(ChatMessageVO vo) {
		mapper.chatMessageInsert(vo);
	}
	/** �ֱ� �޽��� 20�� ��ȸ */
	public List<ChatMessageVO> getLatestMessagesByRoomId(long room_id) {
		return mapper.getLatestMessagesByRoomId(room_id);
	}
	/** room_id �������� */
	public int getNextRoomId() {
		return mapper.getNextRoomId();
	}
	/** �׷� ���� */
	public void createRoom(long room_id, String room_name) {
		mapper.createRoom(room_id, room_name);
	}
	/** �׷� ���� */
	public void subRoom(long room_id, String user_id) {
		mapper.subRoom(room_id, user_id);
	}
}
