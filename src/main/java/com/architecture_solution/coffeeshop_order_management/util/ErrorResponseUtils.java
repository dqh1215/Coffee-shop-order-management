package com.architecture_solution.coffeeshop_order_management.util;

import com.architecture_solution.coffeeshop_order_management.dto.common.ErrorResponse;
import com.architecture_solution.coffeeshop_order_management.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class ErrorResponseUtils {
    private ErrorResponseUtils() {
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String message, String path) {
        ErrorResponse body = ErrorResponse.builder()
                .statusCode(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .error(errorCode.getStatus().getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(HttpStatus status, String message, String path) {
        ErrorResponse body = ErrorResponse.builder()
                .statusCode(status.value())
                .code(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
