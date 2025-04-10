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
	public List<ChatMessageVO> getLatestMessagesByRoomId(int room_id) {
		return mapper.getLatestMessagesByRoomId(room_id);
	}
	/** �׷� ���� */
	public void createRoom(String room_name) {
		mapper.createRoom(room_name);
	}
	/** �׷� ���� */
	public void subRoom(Map<String, Object> map) {
		mapper.subRoom(map);
	}
}
