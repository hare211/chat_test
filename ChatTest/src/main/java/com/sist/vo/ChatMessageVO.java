package com.sist.vo;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatMessageVO {
	private Long m_id, room_id;
	private String sender_id, content;
	private Timestamp sent_at;
	
}
