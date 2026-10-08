package com.architecture_solution.coffeeshop_order_management.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @NotBlank
    @Email
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

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
