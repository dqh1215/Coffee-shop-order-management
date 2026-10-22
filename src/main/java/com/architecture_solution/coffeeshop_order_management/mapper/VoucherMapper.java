package com.architecture_solution.coffeeshop_order_management.mapper;

import com.architecture_solution.coffeeshop_order_management.dto.voucher.request.CreateVoucherRequest;
import com.architecture_solution.coffeeshop_order_management.dto.voucher.response.VoucherResponse;
import com.architecture_solution.coffeeshop_order_management.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    VoucherResponse toResponse(Voucher voucher);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    Voucher toEntity(CreateVoucherRequest request);
}
