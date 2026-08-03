package com.example.kkikki_be_server.domain.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sample")
public class SampleController {

	private final SampleService sampleService;

	@GetMapping
	public SampleDto getSample() {
		return sampleService.getSample();
	}
}