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

    PageResponse<List<OrderSummaryResponse>> getMyOrders(String userId, Pageable pageable);
    PageResponse<List<OrderSummaryResponse>> getOrders(OrderStatus status, String userId,
                                                       LocalDate from, LocalDate to, Pageable pageable);

    OrderResponse cancelOrder(String orderId, CancelOrderRequest request, String userId, boolean admin);
    OrderResponse confirmOrder(String orderId);
    OrderResponse completeOrder(String orderId);
}