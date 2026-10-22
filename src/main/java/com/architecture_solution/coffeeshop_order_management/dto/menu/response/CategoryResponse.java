package com.architecture_solution.coffeeshop_order_management.dto.menu.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String id;

    private String name;
}