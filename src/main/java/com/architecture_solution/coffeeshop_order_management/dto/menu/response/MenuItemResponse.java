package com.architecture_solution.coffeeshop_order_management.dto.menu.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {
    private String id;

    @JsonProperty("category_id")
    private String categoryId;

    private String name;

    private String description;

    private BigDecimal price;

    private boolean available;
}