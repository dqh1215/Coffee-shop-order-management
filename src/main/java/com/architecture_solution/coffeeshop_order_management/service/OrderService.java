package com.architecture_solution.coffeeshop_order_management.service;

import com.architecture_solution.coffeeshop_order_management.dto.common.PageResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.CancelOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.CreateOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.BillResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderSummaryResponse;
import com.architecture_solution.coffeeshop_order_management.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(String userId, CreateOrderRequest request);
    OrderResponse getOrder(String orderId, String userId, boolean admin);
    BillResponse getBill(String orderId, String userId, boolean admin);

    // Customers only see their own orders; userIdFilter is honoured for ADMIN only.
    PageResponse<List<OrderSummaryResponse>> getOrders(String userId, boolean admin, OrderStatus status,
                                                       String userIdFilter, LocalDate from, LocalDate to,
                                                       Pageable pageable);

    OrderResponse cancelOrder(String orderId, CancelOrderRequest request, String userId, boolean admin);
    OrderResponse confirmOrder(String orderId);
}