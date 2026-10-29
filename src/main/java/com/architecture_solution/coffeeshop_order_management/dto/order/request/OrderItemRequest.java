package com.architecture_solution.coffeeshop_order_management.dto.order.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class OrderItemRequest {
    @Schema(example = "913ea87e-d553-408e-b031-478d1cf8f949")
    @NotBlank
    @JsonProperty("menu_item_id")
    private String menuItemId;

    @Schema(example = "2")
    @NotNull
    @Min(1)
    private Integer quantity;
}