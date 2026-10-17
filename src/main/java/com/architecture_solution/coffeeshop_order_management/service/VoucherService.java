package com.architecture_solution.coffeeshop_order_management.service;

import com.architecture_solution.coffeeshop_order_management.dto.voucher.CreateVoucherRequest;
import com.architecture_solution.coffeeshop_order_management.dto.voucher.VoucherResponse;
import com.architecture_solution.coffeeshop_order_management.entity.Voucher;
import com.architecture_solution.coffeeshop_order_management.exception.AppException;
import com.architecture_solution.coffeeshop_order_management.exception.ErrorCode;
import com.architecture_solution.coffeeshop_order_management.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;

    @Transactional
    public VoucherResponse create(CreateVoucherRequest request) {
        String code = normalize(request.getCode());
        if (voucherRepository.existsByCode(code)) {
            throw new AppException(ErrorCode.VOUCHER_ALREADY_EXISTS);
        }
        Voucher voucher = Voucher.builder()
                .code(code)
                .discountPercent(request.getDiscountPercent())
                .expiresAt(request.getExpiresAt())
                .build();
        return VoucherResponse.from(voucherRepository.save(voucher));
    }

    @Transactional(readOnly = true)
    public VoucherResponse getByCode(String code) {
        return VoucherResponse.from(findOrThrow(code));
    }

    @Transactional(readOnly = true)
    public Voucher getValidVoucher(String code) {
        Voucher voucher = findOrThrow(code);
        if (voucher.isExpired()) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }
        return voucher;
    }

    public BigDecimal calculateDiscount(Voucher voucher, BigDecimal subtotal) {
        return subtotal
                .multiply(BigDecimal.valueOf(voucher.getDiscountPercent()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private Voucher findOrThrow(String code) {
        return voucherRepository.findByCodeAndActiveTrue(normalize(code))
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
    }

    private String normalize(String code) {
        return code.trim().toUpperCase();
    }
}