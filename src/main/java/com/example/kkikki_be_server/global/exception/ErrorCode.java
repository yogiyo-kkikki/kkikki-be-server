package com.example.kkikki_be_server.global.exception;

public enum ErrorCode {
	INVALID_REQUEST("INVALID_REQUEST", "The request is invalid"),
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "An unexpected error occurred");

	private final String code;
	private final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}