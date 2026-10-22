package com.architecture_solution.coffeeshop_order_management.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_FAILED(1000, "Validation failed", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1001, "Email already exists", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS(1002, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    PASSWORD_MISMATCH(1003, "Passwords do not match", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(1004, "Resource not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1005, "You need to log in first", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1006, "You don't have permission for this action", HttpStatus.FORBIDDEN),

    VOUCHER_ALREADY_EXISTS(1007, "Voucher code already exists", HttpStatus.CONFLICT),
    VOUCHER_NOT_FOUND(1008, "Voucher not found", HttpStatus.NOT_FOUND),
    VOUCHER_EXPIRED(1009, "Voucher has expired", HttpStatus.BAD_REQUEST),

    ORDER_NOT_FOUND(1100, "Order not found", HttpStatus.NOT_FOUND),
    MENU_ITEM_UNAVAILABLE(1101, "Menu item is not available", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS(1102, "Invalid order status transition", HttpStatus.CONFLICT),
    MENU_ITEM_NOT_FOUND(1103, "Menu item not found", HttpStatus.NOT_FOUND)

    ;

    private final int code;
    private final String message;
    private final HttpStatus status;
}
