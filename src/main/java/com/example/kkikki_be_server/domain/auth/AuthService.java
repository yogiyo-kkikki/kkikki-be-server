package com.example.kkikki_be_server.domain.auth;

import com.example.kkikki_be_server.global.exception.BusinessException;
import com.example.kkikki_be_server.global.exception.ErrorCode;
import com.example.kkikki_be_server.global.security.JwtProvider;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private static final String TOKEN_TYPE = "Bearer";

	private final JdbcTemplate jdbcTemplate;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	private final Map<String, String> refreshTokenStore = new ConcurrentHashMap<>();

	public AuthDto.ExistsResponse exists(String username) {
		return new AuthDto.ExistsResponse(existsByUsername(username));
	}

	@Transactional
	public AuthDto.LoginResponse signup(AuthDto.SignupRequest request) {
		if (existsByUsername(request.username())) {
			throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
		}
		if (existsByEmail(request.email())) {
			throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}

		long now = System.currentTimeMillis() / 1000;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO tb_user (username, email, role, state, password, img_url, c_date, u_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, request.username());
			ps.setString(2, request.email());
			ps.setInt(3, 0);
			ps.setInt(4, 1);
			ps.setString(5, passwordEncoder.encode(request.password()));
			ps.setString(6, request.imgUrl());
			ps.setLong(7, now);
			ps.setLong(8, now);
			return ps;
		}, keyHolder);

		Number createdId = keyHolder.getKey();
		if (createdId == null) {
			throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		}

		UserRow savedUser = findUserById(createdId.longValue());
		AuthDto.TokenPairResponse tokenPair = issueTokens(savedUser.username());
		return new AuthDto.LoginResponse(toUserProfile(savedUser), tokenPair);
	}

	public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
		UserRow user = findUserByUsername(request.username());
		if (!passwordEncoder.matches(request.password(), user.password())) {
			throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
		}

		AuthDto.TokenPairResponse tokenPair = issueTokens(user.username());
		return new AuthDto.LoginResponse(toUserProfile(user), tokenPair);
	}

	@Transactional
	public AuthDto.TokenPairResponse refreshToken(AuthDto.TokenRefreshRequest request) {
		String refreshToken = request.refreshToken();
		if (!jwtProvider.validateRefreshToken(refreshToken)) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN);
		}

		String username = jwtProvider.extractSubject(refreshToken);
		String savedToken = refreshTokenStore.get(username);
		if (savedToken == null) {
			throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
		}
		if (!savedToken.equals(refreshToken)) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN);
		}

		return issueTokens(username);
	}

	@Transactional
	public Map<String, Boolean> logout(String authorizationHeader, AuthDto.LogoutRequest request) {
		String username = null;

		if (request != null && request.refreshToken() != null && !request.refreshToken().isBlank()) {
			if (!jwtProvider.validateRefreshToken(request.refreshToken())) {
				throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN);
			}
			username = jwtProvider.extractSubject(request.refreshToken());
		}

		if (username == null) {
			String accessToken = resolveAccessToken(authorizationHeader);
			if (!jwtProvider.validateAccessToken(accessToken)) {
				throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN);
			}
			username = jwtProvider.extractSubject(accessToken);
		}

		refreshTokenStore.remove(username);
		return Map.of("logout", true);
	}

	public AuthDto.UserProfileResponse getMe(String authorizationHeader) {
		String username = extractAuthorizedUsername(authorizationHeader);
		UserRow user = findUserByUsername(username);
		return toUserProfile(user);
	}

	@Transactional
	public AuthDto.UserProfileResponse update(String authorizationHeader, AuthDto.UpdateRequest request) {
		String currentUsername = extractAuthorizedUsername(authorizationHeader);
		UserRow currentUser = findUserByUsername(currentUsername);

		if (!currentUser.username().equals(request.username()) && existsByUsername(request.username())) {
			throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
		}
		if (!currentUser.email().equals(request.email()) && existsByEmail(request.email())) {
			throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}

		long now = System.currentTimeMillis() / 1000;
		int updated = jdbcTemplate.update(
				"UPDATE tb_user SET username = ?, email = ?, img_url = ?, u_date = ? WHERE id = ?",
				request.username(),
				request.email(),
				request.imgUrl(),
				now,
				currentUser.id());
		if (updated == 0) {
			throw new BusinessException(ErrorCode.USER_NOT_FOUND);
		}

		if (!currentUsername.equals(request.username())) {
			String token = refreshTokenStore.remove(currentUsername);
			if (token != null) {
				refreshTokenStore.put(request.username(), token);
			}
		}

		return toUserProfile(findUserById(currentUser.id()));
	}

	private String extractAuthorizedUsername(String authorizationHeader) {
		String accessToken = resolveAccessToken(authorizationHeader);
		if (!jwtProvider.validateAccessToken(accessToken)) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN);
		}
		return jwtProvider.extractSubject(accessToken);
	}

	private String resolveAccessToken(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN);
		}
		return authorizationHeader.substring("Bearer ".length());
	}

	private AuthDto.TokenPairResponse issueTokens(String username) {
		String accessToken = jwtProvider.createAccessToken(username);
		String refreshToken = jwtProvider.createRefreshToken(username);
		refreshTokenStore.put(username, refreshToken);
		return new AuthDto.TokenPairResponse(
				accessToken,
				refreshToken,
				TOKEN_TYPE,
				jwtProvider.getAccessTokenExpirationSeconds());
	}

	private boolean existsByUsername(String username) {
		Integer count = jdbcTemplate.queryForObject(
				"SELECT COUNT(1) FROM tb_user WHERE username = ?",
				Integer.class,
				username);
		return count != null && count > 0;
	}

	private boolean existsByEmail(String email) {
		Integer count = jdbcTemplate.queryForObject(
				"SELECT COUNT(1) FROM tb_user WHERE email = ?",
				Integer.class,
				email);
		return count != null && count > 0;
	}

	private UserRow findUserByUsername(String username) {
		return jdbcTemplate.query(
				"SELECT id, username, email, role, state, password, img_url, c_date, u_date FROM tb_user WHERE username = ?",
				userRowMapper,
				username)
				.stream()
				.findFirst()
				.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

	private UserRow findUserById(Long id) {
		return jdbcTemplate.query(
				"SELECT id, username, email, role, state, password, img_url, c_date, u_date FROM tb_user WHERE id = ?",
				userRowMapper,
				id)
				.stream()
				.findFirst()
				.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

	private AuthDto.UserProfileResponse toUserProfile(UserRow user) {
		return new AuthDto.UserProfileResponse(
				user.id(),
				user.username(),
				user.email(),
				user.role(),
				user.state(),
				user.imgUrl(),
				user.cDate(),
				user.uDate());
	}

	private final RowMapper<UserRow> userRowMapper = (rs, rowNum) -> new UserRow(
			rs.getLong("id"),
			rs.getString("username"),
			rs.getString("email"),
			rs.getInt("role"),
			rs.getInt("state"),
			rs.getString("password"),
			rs.getString("img_url"),
			rs.getLong("c_date"),
			rs.getLong("u_date"));

	private record UserRow(
			Long id,
			String username,
			String email,
			Integer role,
			Integer state,
			String password,
			String imgUrl,
			Long cDate,
			Long uDate) {
	}
}
