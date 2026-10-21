package com.architecture_solution.coffeeshop_order_management.dto.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    @JsonProperty("menu_item_id")
    private String menuItemId;

    @JsonProperty("item_name")
    private String itemName;

    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

    private Integer quantity;

    @JsonProperty("line_total")
    private BigDecimal lineTotal;
}