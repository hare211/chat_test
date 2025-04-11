package com.sist.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.service.ChatMessageService;
import com.sist.vo.RoomsVO;

@RestController
@RequestMapping("/chat")
public class RoomController {
	
	@Autowired
	private ChatMessageService chatService;
	
	@GetMapping("/room")
	public List<Map<String, String>> getRoom() {
		// 테스트용 그룹
		List<Map<String, String>> rooms = new ArrayList<>();
        
        Map<String, String> room1 = new HashMap<>();
        room1.put("room_id", "1");
        room1.put("name", "자유 채팅방");
        
        Map<String, String> room2 = new HashMap<>();
        room2.put("room_id", "2");
        room2.put("name", "토론방");

        rooms.add(room1);
        rooms.add(room2);
        
        return rooms;
	}
	
	@PostMapping("/create")
	public Map<String, Object> createRoom(@RequestBody Map<String, String> body) {
		String user_id = body.get("user_id");
		String room_name = body.get("room_name");
		
		chatService.createRoom(room_name, user_id);
		
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("result", "success");
		res.put("room_name", room_name);
		res.put("user_id", user_id);
		
		return res;
	}
}
