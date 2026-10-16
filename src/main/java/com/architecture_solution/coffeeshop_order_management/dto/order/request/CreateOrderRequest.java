package com.architecture_solution.coffeeshop_order_management.dto.order.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;

    @Size(max = 255)
    private String note;
}