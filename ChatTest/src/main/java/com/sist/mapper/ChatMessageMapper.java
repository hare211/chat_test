package com.sist.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
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
	public List<ChatMessageVO> getLatestMessagesByRoomId(long room_id);
	
	@Select("SELECT rooms_rid_seq.NEXTVAL FROM dual")
	public int getNextRoomId();
	
	@Insert("INSERT INTO rooms VALUES(#{room_id}, #{room_name})")
	public void createRoom(@Param("room_id") long room_id, @Param("room_name") String room_name);
	
	@Insert("INSERT INTO sub_rooms VALUES(srooms_no_seq.NEXTVAL, #{user_id}, #{room_id})")
	public void subRoom(@Param("room_id") long room_id, @Param("user_id") String user_id);
}
