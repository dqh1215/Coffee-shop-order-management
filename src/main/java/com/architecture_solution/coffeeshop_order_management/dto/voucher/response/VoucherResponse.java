package com.architecture_solution.coffeeshop_order_management.dto.voucher.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {
    private String code;

    @JsonProperty("discount_percent")
    private Integer discountPercent;

    @JsonProperty("expired_at")
    private LocalDateTime expiredAt;

    private boolean expired;
}
