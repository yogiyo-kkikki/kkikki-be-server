package com.example.kkikki_be_server.global.response;

public record MessagePacket(Header hd, Body bd) {

	public record Header(long tid, String event) {
	}

	public record Body(Object data) {
	}
}