package com.architecture_solution.coffeeshop_order_management.dto.auth.response;

import com.architecture_solution.coffeeshop_order_management.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;

    private String email;

    private RoleName role;

    @JsonProperty("expires_at")
    private Instant expiresAt;
}
