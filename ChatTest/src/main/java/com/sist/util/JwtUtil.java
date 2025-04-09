package com.sist.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    
    private final String secretKey = "thisistestkey"; // 테스트용 키
    private final long expiration = 1000 * 60 * 60 * 3; // 3시간

    // token 생성
    public String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        System.out.println("userId: " + userId);
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }
    // 파싱된 token 의 subject 에 있는 userId 반환
    public String getUserIdFromToken(String token) {
        return getClaims(token).getSubject(); // subject에 userId 저장됨
    }

    public boolean validateToken(String token) {
        try {
        	System.out.println("검증 중 토큰: " + token);
        	Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
	// token 파싱하여 payload 정보 추출
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
