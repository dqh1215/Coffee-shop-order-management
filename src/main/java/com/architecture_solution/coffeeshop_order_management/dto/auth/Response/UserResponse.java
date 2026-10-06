package com.architecture_solution.coffeeshop_order_management.dto.auth.Response;

import com.architecture_solution.coffeeshop_order_management.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;

    private String username;

    @JsonProperty("full_name")
    private String fullName;

    private RoleName role;

    private boolean active;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
