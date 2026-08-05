package com.example.kkikki_be_server.infrastructure.messaging.common;

import java.util.LinkedHashMap;
import java.util.Map;

public final class MessageBuilder {

	private MessageBuilder() {
	}

	public static MessagePacket build(String event, Object data) {
		return build(System.currentTimeMillis(), event, data);
	}

	public static MessagePacket build(long tid, String event, Object data) {
		return new MessagePacket(
			new MessagePacket.Header(tid, event),
			new MessagePacket.Body(data)
		);
	}

	public static Map<String, Object> dataOf(Object... keyValues) {
		if (keyValues.length % 2 != 0) {
			throw new IllegalArgumentException("keyValues length must be even");
		}
		Map<String, Object> data = new LinkedHashMap<>();
		for (int i = 0; i < keyValues.length; i += 2) {
			data.put(String.valueOf(keyValues[i]), keyValues[i + 1]);
		}
		return data;
	}
}
