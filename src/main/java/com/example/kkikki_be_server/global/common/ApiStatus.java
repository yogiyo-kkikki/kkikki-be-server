package com.example.kkikki_be_server.global.common;

public enum ApiStatus {
	SUCCESS(200, "S0000", "성공"),
	CREATED(201, "S0001", "리소스가 생성되었습니다"),
	ACCEPTED(202, "S0002", "요청이 접수되었습니다"),

	BAD_REQUEST(400, "E0001", "잘못된 요청입니다"),
	UNAUTHORIZED(401, "E0002", "인증이 필요합니다"),
	FORBIDDEN(403, "E0003", "접근이 거부되었습니다"),
	NOT_FOUND(404, "E0004", "리소스를 찾을 수 없습니다"),
	METHOD_NOT_ALLOWED(405, "E0005", "허용되지 않은 메서드입니다"),
	CONFLICT(409, "E0006", "이미 존재하는 리소스입니다"),
	GONE(410, "E0007", "삭제된 리소스입니다"),
	PAYLOAD_TOO_LARGE(413, "E0008", "요청 데이터가 너무 큽니다"),
	TIMEOUT(408, "E0009", "응답 시간이 초과되었습니다"),
	UNPROCESSABLE(422, "E0010", "유효성 검사에 실패했습니다"),
	TOO_MANY_REQUESTS(429, "E0029", "너무 많은 요청이 발생했습니다"),

	SERVER_ERROR(500, "E0500", "서버 내부 오류가 발생했습니다"),
	NOT_IMPLEMENTED(501, "E0501", "구현되지 않은 기능입니다"),
	BAD_GATEWAY(502, "E0502", "게이트웨이 오류가 발생했습니다"),
	SERVICE_UNAVAILABLE(503, "E0503", "서비스를 일시적으로 사용할 수 없습니다");

	private final int httpCode;
	private final String code;
	private final String msg;

	ApiStatus(int httpCode, String code, String msg) {
		this.httpCode = httpCode;
		this.code = code;
		this.msg = msg;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static ApiStatus fromHttpCode(int httpCode) {
		for (ApiStatus status : values()) {
			if (status.httpCode == httpCode) {
				return status;
			}
		}
		return SERVER_ERROR;
	}
}