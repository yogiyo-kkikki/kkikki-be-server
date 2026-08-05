package com.example.kkikki_be_server.monitoring;

import com.example.kkikki_be_server.global.response.ApiResponse;
import com.example.kkikki_be_server.global.response.ApiResponseBuilder;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/health")
	public ResponseEntity<ApiResponse> health() {
		return ApiResponseBuilder.success(Map.of("health", "UP"));
	}
}