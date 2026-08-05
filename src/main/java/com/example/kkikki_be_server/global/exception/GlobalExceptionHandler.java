package com.example.kkikki_be_server.global.exception;

import com.example.kkikki_be_server.global.response.ApiResponse;
import com.example.kkikki_be_server.global.response.ApiResponseBuilder;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse> handleBusinessException(BusinessException exception) {
		ErrorCode errorCode = exception.getErrorCode();
		return ApiResponseBuilder.error(errorCode.getHttpStatus(), errorCode.getCode(), errorCode.getMessage(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException exception) {
		String message = exception.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
				.orElse("Validation failed");
		return ApiResponseBuilder.error(
				HttpStatus.BAD_REQUEST,
				ErrorCode.INVALID_REQUEST.getCode(),
				message,
				Map.of());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleException(Exception exception) {
		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		return ApiResponseBuilder.error(
				HttpStatus.INTERNAL_SERVER_ERROR,
				errorCode.getCode(),
				errorCode.getMessage(),
				null);
	}
}