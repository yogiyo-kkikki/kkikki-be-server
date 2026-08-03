package com.example.kkikki_be_server.global.security;

import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

	public String createToken(String subject) {
		return subject;
	}

	public String extractSubject(String token) {
		return token;
	}
}