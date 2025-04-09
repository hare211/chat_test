package com.sist.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class AuthController {
	private final JwtUtil jwtUtil;
	
	public AuthController(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	@GetMapping("/token")
	public String generateToken() {
		return jwtUtil.generateToken("user"); // 테스트용 user 정보
	}
}
