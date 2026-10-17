package com.architecture_solution.coffeeshop_order_management.dto.voucher;

import com.architecture_solution.coffeeshop_order_management.entity.Voucher;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VoucherResponse {
    private String code;
    private Integer discountPercent;
    private LocalDateTime expiresAt;
    private boolean expired;

    public static VoucherResponse from(Voucher v) {
        return VoucherResponse.builder()
                .code(v.getCode())
                .discountPercent(v.getDiscountPercent())
                .expiresAt(v.getExpiresAt())
                .expired(v.isExpired())
                .build();
    }
}