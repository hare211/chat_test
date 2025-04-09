package com.sist.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    
    private final String secretKey = "thisistestkey"; // �׽�Ʈ�� Ű
    private final long expiration = 1000 * 60 * 60 * 3; // 3�ð�

    // token ����
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
    // �Ľ̵� token �� subject �� �ִ� userId ��ȯ
    public String getUserIdFromToken(String token) {
        return getClaims(token).getSubject(); // subject�� userId �����
    }

    public boolean validateToken(String token) {
        try {
        	System.out.println("���� �� ��ū: " + token);
        	Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
	// token �Ľ��Ͽ� payload ���� ����
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
