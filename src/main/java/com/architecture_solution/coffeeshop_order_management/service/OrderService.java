package com.architecture_solution.coffeeshop_order_management.service;

import com.architecture_solution.coffeeshop_order_management.dto.order.request.CreateOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(String userId, CreateOrderRequest request);
}
