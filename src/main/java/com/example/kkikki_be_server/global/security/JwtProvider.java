package com.example.kkikki_be_server.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

	private static final String TOKEN_TYPE_CLAIM = "type";
	private static final String ACCESS_TOKEN_TYPE = "ACCESS";
	private static final String REFRESH_TOKEN_TYPE = "REFRESH";

	private final SecretKey secretKey;
	private final long accessTokenExpirationSeconds;
	private final long refreshTokenExpirationSeconds;

	public JwtProvider(
			@Value("${jwt.secret:kkikki-secret-key-for-local-development-please-change-it}") String secret,
			@Value("${jwt.access-token-expiration-seconds:3600}") long accessTokenExpirationSeconds,
			@Value("${jwt.refresh-token-expiration-seconds:1209600}") long refreshTokenExpirationSeconds) {
		this.secretKey = buildKey(secret);
		this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
		this.refreshTokenExpirationSeconds = refreshTokenExpirationSeconds;
	}

	public String createToken(String subject) {
		return createAccessToken(subject);
	}

	public String createAccessToken(String subject) {
		return createToken(subject, ACCESS_TOKEN_TYPE, accessTokenExpirationSeconds);
	}

	public String createRefreshToken(String subject) {
		return createToken(subject, REFRESH_TOKEN_TYPE, refreshTokenExpirationSeconds);
	}

	public String extractSubject(String token) {
		return parseClaims(token).getSubject();
	}

	public Long extractUserId(String token) {
		return Long.valueOf(extractSubject(token));
	}

	public boolean validateAccessToken(String token) {
		return validateByType(token, ACCESS_TOKEN_TYPE);
	}

	public boolean validateRefreshToken(String token) {
		return validateByType(token, REFRESH_TOKEN_TYPE);
	}

	public long getAccessTokenExpirationSeconds() {
		return accessTokenExpirationSeconds;
	}

	private String createToken(String subject, String type, long expiresInSeconds) {
		Date now = new Date();
		Date expiredAt = new Date(now.getTime() + (expiresInSeconds * 1000));

		return Jwts.builder()
				.setId(UUID.randomUUID().toString())
				.setSubject(subject)
				.claim(TOKEN_TYPE_CLAIM, type)
				.setIssuedAt(now)
				.setExpiration(expiredAt)
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
	}

	private boolean validateByType(String token, String requiredType) {
		try {
			Claims claims = parseClaims(token);
			Long.parseLong(claims.getSubject());
			return requiredType.equals(claims.get(TOKEN_TYPE_CLAIM, String.class));
		} catch (Exception ignored) {
			return false;
		}
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private SecretKey buildKey(String secret) {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		if (keyBytes.length < 32) {
			byte[] padded = new byte[32];
			System.arraycopy(keyBytes, 0, padded, 0, keyBytes.length);
			keyBytes = padded;
		}
		Key key = Keys.hmacShaKeyFor(keyBytes);
		return (SecretKey) key;
	}
}
