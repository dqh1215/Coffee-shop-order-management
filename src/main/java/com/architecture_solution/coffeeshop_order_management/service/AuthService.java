package com.architecture_solution.coffeeshop_order_management.service;

import com.architecture_solution.coffeeshop_order_management.dto.auth.request.LoginRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.request.RegisterRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.response.LoginResponse;
import com.architecture_solution.coffeeshop_order_management.dto.auth.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
