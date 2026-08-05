package com.example.kkikki_be_server.global.response;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ApiResponseBuilder {

	private ApiResponseBuilder() {
	}

	public static ResponseEntity<ApiResponse> success(Object data) {
		return withStatus(ApiStatus.SUCCESS, data);
	}

	public static ResponseEntity<ApiResponse> withStatus(ApiStatus status, Object data) {
		ApiResponse response = new ApiResponse(
				System.currentTimeMillis(),
				new ApiResponse.StatusInfo(status.getCode(), status.getMsg()),
				normalizeData(data));

		return ResponseEntity.status(status.getHttpCode()).body(response);
	}

	public static ResponseEntity<ApiResponse> errorByHttpStatus(HttpStatus httpStatus, Object data) {
		ApiStatus status = ApiStatus.fromHttpCode(httpStatus.value());
		return withStatus(status, data);
	}

	public static ResponseEntity<ApiResponse> error(HttpStatus httpStatus, String code, String msg, Object data) {
		ApiResponse response = new ApiResponse(
				System.currentTimeMillis(),
				new ApiResponse.StatusInfo(code, msg),
				normalizeData(data));

		return ResponseEntity.status(httpStatus).body(response);
	}

	private static Object normalizeData(Object data) {
		return data == null ? Map.of() : data;
	}
}