package com.architecture_solution.coffeeshop_order_management.service;

import com.architecture_solution.coffeeshop_order_management.dto.auth.response.UserResponse;

public interface UserService {
    UserResponse getCurrentUser(String userId);
}
