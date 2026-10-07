package com.architecture_solution.coffeeshop_order_management.exception;

import com.architecture_solution.coffeeshop_order_management.dto.common.ErrorResponse;
import com.architecture_solution.coffeeshop_order_management.util.ErrorResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUncaught(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception at {}", request.getRequestURI(), ex);
        return ErrorResponseUtils.toResponseEntity(ErrorCode.UNCATEGORIZED_EXCEPTION, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
        log.error(" {}", request.getRequestURI(), ex);
        return ErrorResponseUtils.toResponseEntity(ex.getErrorCode(), ex.getErrorCode().getMessage(),  request.getRequestURI());
    }


}
