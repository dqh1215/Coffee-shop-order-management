package com.architecture_solution.coffeeshop_order_management.service;

import com.architecture_solution.coffeeshop_order_management.dto.auth.Request.LoginRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.Request.RegisterRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.Response.LoginResponse;
import com.architecture_solution.coffeeshop_order_management.dto.auth.Response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
