package com.architecture_solution.coffeeshop_order_management.controller;

import com.architecture_solution.coffeeshop_order_management.config.openapi.BadRequestResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.ConflictResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.CreatedResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.OkResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.UnauthorizedResponse;
import com.architecture_solution.coffeeshop_order_management.dto.auth.request.LoginRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.request.RegisterRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.response.LoginResponse;
import com.architecture_solution.coffeeshop_order_management.dto.common.ApiResponse;
import com.architecture_solution.coffeeshop_order_management.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Auth", description = "Đăng ký và đăng nhập")
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Đăng ký tài khoản",
            description = "Public. Tài khoản mới luôn có role CUSTOMER. 400 nếu password và confirm_password không khớp, 409 nếu email đã tồn tại.")
    @SecurityRequirements
    @CreatedResponse
    @BadRequestResponse
    @ConflictResponse
    @PostMapping("/register")
    ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("registering user: {}", request.getEmail());
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(null, "User registered successfully"));
    }

    @Operation(summary = "Đăng nhập",
            description = "Public. Trả về access_token (JWT); bấm Authorize và dán token để gọi các endpoint cần đăng nhập.")
    @OkResponse
    @SecurityRequirements
    @BadRequestResponse
    @UnauthorizedResponse
    @PostMapping("/login")
    ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("logging user: {}", request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }
}
