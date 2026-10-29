package com.architecture_solution.coffeeshop_order_management.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Schema(example = "customer@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(example = "0901234567")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Schema(example = "password123")
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @Schema(example = "password123")
    @NotBlank
    @JsonProperty("confirm_password")
    private String confirmPassword;

    @Schema(example = "Nguyen Van A")
    @JsonProperty("full_name")
    @NotBlank
    @Size(max = 100)
    private String fullName;
}
