package com.architecture_solution.coffeeshop_order_management.dto.order.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderRequest {
    @Size(max = 255)
    @JsonProperty("cancel_reason")
    private String cancelReason;
}
