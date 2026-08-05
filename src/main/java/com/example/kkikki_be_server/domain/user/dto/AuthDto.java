package com.example.kkikki_be_server.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class AuthDto {

	private AuthDto() {
	}

	public record ExistsResponse(boolean exists) {
	}

	public record SignupRequest(
			@NotBlank(message = "username is required") String username,
			@Email(message = "email format is invalid") @NotBlank(message = "email is required") String email,
			@NotBlank(message = "password is required") String password,
			String imgUrl) {
	}

	public record LoginRequest(
			@NotBlank(message = "username is required") String username,
			@NotBlank(message = "password is required") String password) {
	}

	public record TokenRefreshRequest(@NotBlank(message = "refreshToken is required") String refreshToken) {
	}

	public record LogoutRequest(String refreshToken) {
	}

	public record UpdateRequest(
			@NotBlank(message = "username is required") String username,
			@Email(message = "email format is invalid") @NotBlank(message = "email is required") String email,
			String imgUrl) {
	}

	public record TokenPairResponse(String accessToken, String refreshToken, String tokenType, long expiresIn) {
	}

	public record UserProfileResponse(
			Long id,
			String username,
			String email,
			Integer role,
			Integer state,
			String imgUrl,
			Long cDate,
			Long uDate) {
	}

	public record LoginResponse(UserProfileResponse user, TokenPairResponse token) {
	}
}
