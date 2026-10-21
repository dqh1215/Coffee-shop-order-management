package com.architecture_solution.coffeeshop_order_management.dto.order.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class OrderItemRequest {
    @NotBlank
    @JsonProperty("menu_item_id")
    private String menuItemId;

    @NotNull
    @Min(1)
    private Integer quantity;
}