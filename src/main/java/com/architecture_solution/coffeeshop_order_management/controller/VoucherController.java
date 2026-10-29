package com.architecture_solution.coffeeshop_order_management.controller;

import com.architecture_solution.coffeeshop_order_management.config.openapi.BadRequestResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.ConflictResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.CreatedResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.ForbiddenResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.NotFoundResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.OkResponse;
import com.architecture_solution.coffeeshop_order_management.dto.common.ApiResponse;
import com.architecture_solution.coffeeshop_order_management.dto.voucher.request.CreateVoucherRequest;
import com.architecture_solution.coffeeshop_order_management.dto.voucher.response.VoucherResponse;
import com.architecture_solution.coffeeshop_order_management.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Voucher", description = "Mã giảm giá")
@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @Operation(summary = "Tạo voucher", description = "ADMIN. 409 nếu mã đã tồn tại.")
    @CreatedResponse
    @BadRequestResponse
    @ForbiddenResponse
    @ConflictResponse
    @PostMapping
    public ResponseEntity<ApiResponse<VoucherResponse>> create(@Valid @RequestBody CreateVoucherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(voucherService.create(request), "Voucher created successfully"));
    }

    @Operation(summary = "Tra cứu voucher theo mã", description = "Cần đăng nhập.")
    @OkResponse
    @NotFoundResponse
    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<VoucherResponse>> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.success(voucherService.getByCode(code)));
    }
}
