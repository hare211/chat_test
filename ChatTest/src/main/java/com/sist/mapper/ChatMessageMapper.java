package com.sist.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.sist.vo.ChatMessageVO;

public interface ChatMessageMapper {
	@Insert("INSERT INTO chat_message (m_id, room_id, sender_id, content) VALUES(cm_id_seq.nextval, #{room_id}, #{sender_id}, #{content})")
	public void chatMessageInsert(ChatMessageVO vo);
	
	@Select("SELECT * "
		  + "FROM (SELECT * "
		  		+ "FROM chat_message "
		  		+ "WHERE room_id = #{room_id} "
		  		+ "ORDER BY sent_at DESC) "
  		  + "WHERE ROWNUM <= 20 ORDER BY sent_at ASC")
	public List<ChatMessageVO> getLatestMessagesByRoomId(int room_id);
	
	@Insert("INSERT INTO rooms VALUES(rooms_rid_seq.nextval, #{room_name})")
	public void createRoom(String roo_name);
	
	@Insert("INSERT INTO sub_rooms VALUES(srooms_no_seq, #{user_id}, #{room_id})")
	public void subRoom(Map<String, Object> map);
}
