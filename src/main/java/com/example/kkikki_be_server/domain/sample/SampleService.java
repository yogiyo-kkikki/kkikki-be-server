package com.example.kkikki_be_server.domain.sample;

import org.springframework.stereotype.Service;

@Service
public class SampleService {

	public SampleDto getSample() {
		return new SampleDto("sample");
	}
}