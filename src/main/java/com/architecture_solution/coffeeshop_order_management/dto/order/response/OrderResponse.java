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
public class OrderResponse {
    private String id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("voucher_id")
    private String voucherId;

    private OrderStatus status;

    private BigDecimal subtotal;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @JsonProperty("tax_amount")
    private BigDecimal taxAmount;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    private String note;

    @JsonProperty("cancel_reason")
    private String cancelReason;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;
}