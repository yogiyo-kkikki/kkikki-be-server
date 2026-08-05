package com.example.kkikki_be_server.global.common;

public record ApiResponse(long tid, StatusInfo status, Object data) {

	public record StatusInfo(String code, String msg) {
	}
}