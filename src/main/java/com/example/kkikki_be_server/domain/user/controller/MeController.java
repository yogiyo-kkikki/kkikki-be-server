package com.example.kkikki_be_server.domain.user.controller;

import com.example.kkikki_be_server.domain.user.service.AuthService;
import com.example.kkikki_be_server.global.common.ApiResponse;
import com.example.kkikki_be_server.global.common.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MeController {

	private final AuthService authService;

	@GetMapping("/me")
	public ResponseEntity<ApiResponse> getMe(@RequestHeader("Authorization") String authorization) {
		return ApiResponseBuilder.success(authService.getMe(authorization));
	}
}
