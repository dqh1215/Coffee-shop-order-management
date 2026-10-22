package com.architecture_solution.coffeeshop_order_management.service;

import com.architecture_solution.coffeeshop_order_management.dto.voucher.request.CreateVoucherRequest;
import com.architecture_solution.coffeeshop_order_management.dto.voucher.response.VoucherResponse;
import com.architecture_solution.coffeeshop_order_management.entity.Voucher;

public interface VoucherService {
    VoucherResponse create(CreateVoucherRequest request);

    VoucherResponse getByCode(String code);

    /*
     * Dùng nội bộ khi tạo order: trả về voucher còn hiệu lực, nếu không thì ném AppException.
     */
    Voucher getValidVoucher(String code);
}
