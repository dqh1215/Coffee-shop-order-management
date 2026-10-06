package com.architecture_solution.coffeeshop_order_management.dto.auth.Response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private UserResponse user;
}
