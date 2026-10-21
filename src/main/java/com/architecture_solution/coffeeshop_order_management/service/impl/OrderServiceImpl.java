package com.architecture_solution.coffeeshop_order_management.service.impl;

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
import com.architecture_solution.coffeeshop_order_management.service.OrderService;
import com.architecture_solution.coffeeshop_order_management.util.OrderPricing;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;
    @Value("${app.pricing.tax-rate:0}")
    private BigDecimal taxRate;

    private static OrderItem buildOrderItem(OrderItemRequest line, MenuItem menuItem) {
        if (!menuItem.isActive() || !menuItem.isAvailable()) {
            throw new AppException(ErrorCode.MENU_ITEM_UNAVAILABLE);
        }

        OrderItem item = new OrderItem();
        item.setMenuItemId(menuItem.getId());
        item.setItemName(menuItem.getName());
        item.setUnitPrice(menuItem.getPrice());
        item.setQuantity(line.getQuantity());
        item.setLineTotal(OrderPricing.lineTotal(menuItem.getPrice(), line.getQuantity()));
        return item;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(String userId, CreateOrderRequest request) {
        List<String> menuItemIds = request.getItems().stream()
                .map(OrderItemRequest::getMenuItemId)
                .toList();
        Map<String, MenuItem> menuItems = menuItemRepository.findAllById(menuItemIds).stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));

        List<OrderItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItemRequest orderItemRequest : request.getItems()) {
            MenuItem menuItem = menuItems.get(orderItemRequest.getMenuItemId());
            if (menuItem == null) {
                throw new AppException(ErrorCode.MENU_ITEM_NOT_FOUND);
            }
            OrderItem item = buildOrderItem(orderItemRequest, menuItem);

            items.add(item);
            subtotal = subtotal.add(item.getLineTotal());
        }

        // No voucher support yet, so discountPercent is null.
        OrderPricing.OrderTotals totals = OrderPricing.calculate(subtotal, null, taxRate);

        Order order = Order.builder().userId(userId).note(request.getNote())
                .subtotal(totals.subtotal())
                .discountAmount(totals.discountAmount())
                .taxAmount(totals.taxAmount())
                .totalAmount(totals.totalAmount())
                .build();

        items.forEach(order::addItem);

        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }
}
