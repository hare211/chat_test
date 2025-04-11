package com.sist.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
	private final JwtUtil jwtUtil;
	
	@GetMapping("/token")
	public String generateToken() {
		return jwtUtil.generateToken("user"); // 테스트용 user 정보
	}
}
