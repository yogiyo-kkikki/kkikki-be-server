package com.example.kkikki_be_server.global.common;

public record MessagePacket(Header hd, Body bd) {

	public record Header(long tid, String event) {
	}

	public record Body(Object data) {
	}
}