package com.architecture_solution.coffeeshop_order_management.controller;

import com.architecture_solution.coffeeshop_order_management.dto.common.ApiResponse;
import com.architecture_solution.coffeeshop_order_management.dto.voucher.request.CreateVoucherRequest;
import com.architecture_solution.coffeeshop_order_management.dto.voucher.response.VoucherResponse;
import com.architecture_solution.coffeeshop_order_management.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @PostMapping
    public ResponseEntity<ApiResponse<VoucherResponse>> create(@Valid @RequestBody CreateVoucherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(voucherService.create(request), "Voucher created successfully"));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<VoucherResponse>> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.success(voucherService.getByCode(code)));
    }
}
