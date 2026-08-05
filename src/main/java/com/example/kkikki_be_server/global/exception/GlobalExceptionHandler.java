package com.example.kkikki_be_server.global.exception;

import com.example.kkikki_be_server.global.common.ApiResponse;
import com.example.kkikki_be_server.global.common.ApiResponseBuilder;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ApiResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
		ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;
		return ApiResponseBuilder.error(
				errorCode.getHttpStatus(),
				errorCode.getCode(),
				errorCode.getMessage(),
				null);
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
