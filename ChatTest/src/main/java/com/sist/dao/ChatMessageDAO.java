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
	/** 메시지 저장 */
	public void chatMessageInsert(ChatMessageVO vo) {
		mapper.chatMessageInsert(vo);
	}
	/** 최근 메시지 20개 조회 */
	public List<ChatMessageVO> getLatestMessagesByRoomId(int room_id) {
		return mapper.getLatestMessagesByRoomId(room_id);
	}
	/** 그룹 생성 */
	public void createRoom(String room_name) {
		mapper.createRoom(room_name);
	}
	/** 그룹 구독 */
	public void subRoom(Map<String, Object> map) {
		mapper.subRoom(map);
	}
}
