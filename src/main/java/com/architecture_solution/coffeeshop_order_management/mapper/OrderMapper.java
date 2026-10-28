package com.architecture_solution.coffeeshop_order_management.mapper;

import com.architecture_solution.coffeeshop_order_management.dto.order.response.BillResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderItemResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderSummaryResponse;
import com.architecture_solution.coffeeshop_order_management.entity.Order;
import com.architecture_solution.coffeeshop_order_management.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "orderStatus", target = "status")
    OrderResponse toResponse(Order order);

    @Mapping(source = "orderStatus", target = "status")
    OrderSummaryResponse toSummary(Order order);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderStatus", target = "status")
    BillResponse toBill(Order order);

    OrderItemResponse toItemResponse(OrderItem item);
}
