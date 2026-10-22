package com.architecture_solution.coffeeshop_order_management.service.impl;

import com.architecture_solution.coffeeshop_order_management.dto.voucher.request.CreateVoucherRequest;
import com.architecture_solution.coffeeshop_order_management.dto.voucher.response.VoucherResponse;
import com.architecture_solution.coffeeshop_order_management.entity.Voucher;
import com.architecture_solution.coffeeshop_order_management.exception.AppException;
import com.architecture_solution.coffeeshop_order_management.exception.ErrorCode;
import com.architecture_solution.coffeeshop_order_management.mapper.VoucherMapper;
import com.architecture_solution.coffeeshop_order_management.repository.VoucherRepository;
import com.architecture_solution.coffeeshop_order_management.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    @Override
    @Transactional
    public VoucherResponse create(CreateVoucherRequest request) {
        String code = normalize(request.getCode());
        if (voucherRepository.existsByCode(code)) {
            throw new AppException(ErrorCode.VOUCHER_ALREADY_EXISTS);
        }
        Voucher voucher = voucherMapper.toEntity(request);
        voucher.setCode(code);
        return voucherMapper.toResponse(voucherRepository.save(voucher));
    }

    @Override
    @Transactional(readOnly = true)
    public VoucherResponse getByCode(String code) {
        return voucherMapper.toResponse(findOrThrow(code));
    }

    @Override
    @Transactional(readOnly = true)
    public Voucher getValidVoucher(String code) {
        Voucher voucher = findOrThrow(code);
        if (voucher.isExpired()) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }
        return voucher;
    }

    private Voucher findOrThrow(String code) {
        return voucherRepository.findByCodeAndActiveTrue(normalize(code))
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
    }

    private String normalize(String code) {
        return code.trim().toUpperCase();
    }
}
