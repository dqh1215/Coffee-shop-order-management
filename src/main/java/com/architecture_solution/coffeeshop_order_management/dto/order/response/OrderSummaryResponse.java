package com.architecture_solution.coffeeshop_order_management.dto.order.response;

import com.architecture_solution.coffeeshop_order_management.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryResponse {
    private String id;
    private OrderStatus status;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    private String note;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}