package com.architecture_solution.coffeeshop_order_management.dto.menu.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {
    @Schema(example = "f78bf2e1-2e86-44a9-bd30-f49545384808")
    @NotBlank
    @JsonProperty("category_id")
    private String categoryId;

    @Schema(example = "Cappuccino")
    @NotBlank
    @Size(max = 150)
    private String name;

    @Schema(example = "Espresso with steamed milk foam")
    @Size(max = 500)
    private String description;

    @Schema(example = "45000")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    @Schema(description = "Mặc định true khi tạo mới; không gửi thì giữ nguyên khi cập nhật", example = "true")
    private Boolean available;
}