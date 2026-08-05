package com.example.kkikki_be_server.domain.sample;

public record SampleQueueEnqueueResponse(
	long tid,
	String event,
	String exchange,
	String routingKey,
	String queue
) {
}
