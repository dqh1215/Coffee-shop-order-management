package com.architecture_solution.coffeeshop_order_management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;


}
