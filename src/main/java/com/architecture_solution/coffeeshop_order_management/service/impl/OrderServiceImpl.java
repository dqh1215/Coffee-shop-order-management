package com.architecture_solution.coffeeshop_order_management.service.impl;

import com.architecture_solution.coffeeshop_order_management.dto.order.request.CancelOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.CreateOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.OrderItemRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderResponse;
import com.architecture_solution.coffeeshop_order_management.entity.MenuItem;
import com.architecture_solution.coffeeshop_order_management.entity.Order;
import com.architecture_solution.coffeeshop_order_management.entity.OrderItem;
import com.architecture_solution.coffeeshop_order_management.exception.AppException;
import com.architecture_solution.coffeeshop_order_management.exception.ErrorCode;
import com.architecture_solution.coffeeshop_order_management.mapper.OrderMapper;
import com.architecture_solution.coffeeshop_order_management.repository.MenuItemRepository;
import com.architecture_solution.coffeeshop_order_management.repository.OrderRepository;
import com.architecture_solution.coffeeshop_order_management.security.CustomUserDetails;
import com.architecture_solution.coffeeshop_order_management.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;
    @Value("${app.order.tax-rate:0}")
    private BigDecimal taxRate;

    private static OrderItem buildOrderItem(OrderItemRequest line, MenuItem menuItem) {
        if (!menuItem.isActive() || !menuItem.isAvailable()) {
            throw new AppException(ErrorCode.MENU_ITEM_UNAVAILABLE);
        }
        BigDecimal lineTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(line.getQuantity()));

        OrderItem item = new OrderItem();
        item.setMenuItemId(menuItem.getId());
        item.setItemName(menuItem.getName());
        item.setUnitPrice(menuItem.getPrice());
        item.setQuantity(line.getQuantity());
        item.setLineTotal(lineTotal);
        return item;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(String userId, CreateOrderRequest request) {
        List<OrderItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItemRequest orderItemRequest : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(orderItemRequest.getMenuItemId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
            OrderItem item = buildOrderItem(orderItemRequest, menuItem);

            items.add(item);
            subtotal = subtotal.add(item.getLineTotal());
        }

        BigDecimal taxAmount = subtotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = subtotal.add(taxAmount);

        Order order = Order.builder().userId(userId).note(request.getNote()).subtotal(subtotal)
                .taxAmount(taxAmount).totalAmount(totalAmount).build();

        items.forEach(order::addItem);


        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }



//    @Override
//    @Transactional
//    public OrderResponse cancelOrder(String orderId, CancelOrderRequest request, CustomUserDetails principal) {
//
//    }
}
