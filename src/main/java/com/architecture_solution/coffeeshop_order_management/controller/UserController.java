package com.architecture_solution.coffeeshop_order_management.controller;

import com.architecture_solution.coffeeshop_order_management.dto.auth.Request.LoginRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.Request.RegisterRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.Response.LoginResponse;
import com.architecture_solution.coffeeshop_order_management.dto.auth.Response.UserResponse;
import com.architecture_solution.coffeeshop_order_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;

    @PostMapping("/auth/register")
    UserResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/auth/login")
    LoginResponse getUsers(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
