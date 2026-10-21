package com.architecture_solution.coffeeshop_order_management.dto.order.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderRequest {
    @NotBlank
    @Size(max = 255)
    private String reason;
}