package com.architecture_solution.coffeeshop_order_management.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank
    @JsonProperty("confirm_password")
    private String confirmPassword;

    @JsonProperty("full_name")
    @NotBlank
    @Size(max = 100)
    private String fullName;
}
