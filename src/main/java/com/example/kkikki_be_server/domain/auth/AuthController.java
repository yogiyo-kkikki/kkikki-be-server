package com.example.kkikki_be_server.domain.auth;

import com.example.kkikki_be_server.global.response.ApiResponse;
import com.example.kkikki_be_server.global.response.ApiResponseBuilder;
import com.example.kkikki_be_server.global.response.ApiStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	@GetMapping("/exists")
	public ResponseEntity<ApiResponse> exists(@RequestParam String username) {
		return ApiResponseBuilder.success(authService.exists(username));
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> signup(@Valid @RequestBody AuthDto.SignupRequest request) {
		return ApiResponseBuilder.withStatus(ApiStatus.CREATED, authService.signup(request));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthDto.LoginRequest request) {
		return ApiResponseBuilder.success(authService.login(request));
	}

	@PostMapping("/token")
	public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody AuthDto.TokenRefreshRequest request) {
		return ApiResponseBuilder.success(authService.refreshToken(request));
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse> logout(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody(required = false) AuthDto.LogoutRequest request) {
		return ApiResponseBuilder.success(authService.logout(authorization, request));
	}

	@PostMapping("/update")
	public ResponseEntity<ApiResponse> update(
			@RequestHeader("Authorization") String authorization,
			@Valid @RequestBody AuthDto.UpdateRequest request) {
		return ApiResponseBuilder.success(authService.update(authorization, request));
	}
}
