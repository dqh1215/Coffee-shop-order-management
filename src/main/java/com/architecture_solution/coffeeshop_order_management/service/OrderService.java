package com.architecture_solution.coffeeshop_order_management.service;

import com.architecture_solution.coffeeshop_order_management.dto.order.request.CancelOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.CreateOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderResponse;
import com.architecture_solution.coffeeshop_order_management.security.CustomUserDetails;

public interface OrderService {
    OrderResponse createOrder(String userId, CreateOrderRequest request);
//    OrderResponse cancelOrder(String orderId, CancelOrderRequest request, CustomUserDetails principal);
}
