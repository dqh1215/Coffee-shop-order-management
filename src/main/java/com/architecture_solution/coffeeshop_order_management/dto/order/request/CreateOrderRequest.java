package com.architecture_solution.coffeeshop_order_management.dto.order.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotEmpty
    @Valid
    private List<@NotNull OrderItemRequest> items;

    @Size(max = 255)
    private String note;

    @Size(max = 30)
    @JsonProperty("voucher_code")
    private String voucherCode;

    @AssertTrue(message = "menu_item_id must be unique")
    @JsonIgnore
    public boolean isItemsUnique() {
        if (items == null) return true;
        return items.stream()
                .filter(Objects::nonNull)
                .map(OrderItemRequest::getMenuItemId)
                .distinct()
                .count() == items.stream().filter(Objects::nonNull).count();
    }
}