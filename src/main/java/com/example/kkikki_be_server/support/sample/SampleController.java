package com.example.kkikki_be_server.support.sample;

import com.example.kkikki_be_server.global.common.ApiResponse;
import com.example.kkikki_be_server.global.common.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("local")
@RequiredArgsConstructor
@RequestMapping("/sample")
public class SampleController {

	private final SampleService sampleService;

	@GetMapping
	public ResponseEntity<ApiResponse> getSample() {
		return ApiResponseBuilder.success(sampleService.getSample());
	}

	@PostMapping("/messages/test")
	public ResponseEntity<ApiResponse> enqueueTestMessage() {
		return ApiResponseBuilder.success(sampleService.enqueueTestMessage());
	}
}
