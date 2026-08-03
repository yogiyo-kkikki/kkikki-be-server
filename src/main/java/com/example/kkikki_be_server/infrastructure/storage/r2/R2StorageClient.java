package com.example.kkikki_be_server.infrastructure.storage.r2;

import org.springframework.stereotype.Component;

@Component
public class R2StorageClient {

	public String upload(Object content) {
		return "uploaded";
	}
}