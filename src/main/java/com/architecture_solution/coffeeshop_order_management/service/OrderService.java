package com.architecture_solution.coffeeshop_order_management.service;

import com.architecture_solution.coffeeshop_order_management.dto.common.PageResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.CancelOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.CreateOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderSummaryResponse;
import com.architecture_solution.coffeeshop_order_management.enums.OrderStatus;
import com.architecture_solution.coffeeshop_order_management.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(String userId, CreateOrderRequest request);
    OrderResponse getOrder(String orderId, CustomUserDetails principal);

    PageResponse<List<OrderSummaryResponse>> getMyOrders(String userId, Pageable pageable);
    PageResponse<List<OrderSummaryResponse>> getOrders(OrderStatus status, String userId,
                                                       LocalDate from, LocalDate to, Pageable pageable);

    OrderResponse cancelOrder(String orderId, CancelOrderRequest request, CustomUserDetails principal);
    OrderResponse confirmOrder(String orderId);
    OrderResponse completeOrder(String orderId);
}