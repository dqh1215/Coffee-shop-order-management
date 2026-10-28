package com.architecture_solution.coffeeshop_order_management.dto.order.response;

import com.architecture_solution.coffeeshop_order_management.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    @JsonProperty("order_id")
    private String orderId;

    private OrderStatus status;

    private List<OrderItemResponse> items;

    private BigDecimal subtotal;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @JsonProperty("tax_amount")
    private BigDecimal taxAmount;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
