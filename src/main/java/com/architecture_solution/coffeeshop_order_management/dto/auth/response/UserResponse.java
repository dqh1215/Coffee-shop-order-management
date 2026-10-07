package com.architecture_solution.coffeeshop_order_management.dto.auth.response;

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
    private String id;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("full_name")
    private String fullName;

    private RoleName role;

    private boolean active;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
