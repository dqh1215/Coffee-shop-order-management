package com.architecture_solution.coffeeshop_order_management.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Schema(example = "customer@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(example = "password123")
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}
