package com.sist.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class RoomController {
	@GetMapping("/room")
	public List<Map<String, String>> getRoom() {
		// �׽�Ʈ�� �׷�
		List<Map<String, String>> rooms = new ArrayList<>();
        
        Map<String, String> room1 = new HashMap<>();
        room1.put("roomId", "room1");
        room1.put("name", "���� ä�ù�");
        
        Map<String, String> room2 = new HashMap<>();
        room2.put("roomId", "room2");
        room2.put("name", "��й�");

        rooms.add(room1);
        rooms.add(room2);
        
        return rooms;
	}
}
