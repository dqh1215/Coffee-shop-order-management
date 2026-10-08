package com.architecture_solution.coffeeshop_order_management.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_ALREADY_EXISTS(1001, "Email already exists", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS(1002, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    PASSWORD_MISMATCH(1003, "Passwords do not match", HttpStatus.BAD_REQUEST)
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;
}
