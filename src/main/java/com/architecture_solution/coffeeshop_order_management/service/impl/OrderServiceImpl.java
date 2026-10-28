package com.architecture_solution.coffeeshop_order_management.service.impl;

import com.architecture_solution.coffeeshop_order_management.dto.common.PageResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.CancelOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.CreateOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.OrderItemRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.BillResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderSummaryResponse;
import com.architecture_solution.coffeeshop_order_management.entity.MenuItem;
import com.architecture_solution.coffeeshop_order_management.entity.Order;
import com.architecture_solution.coffeeshop_order_management.entity.OrderItem;
import com.architecture_solution.coffeeshop_order_management.entity.Voucher;
import com.architecture_solution.coffeeshop_order_management.enums.OrderStatus;
import com.architecture_solution.coffeeshop_order_management.exception.AppException;
import com.architecture_solution.coffeeshop_order_management.exception.ErrorCode;
import com.architecture_solution.coffeeshop_order_management.mapper.OrderMapper;
import com.architecture_solution.coffeeshop_order_management.repository.MenuItemRepository;
import com.architecture_solution.coffeeshop_order_management.repository.OrderRepository;
import com.architecture_solution.coffeeshop_order_management.repository.OrderSpecifications;
import com.architecture_solution.coffeeshop_order_management.service.OrderService;
import com.architecture_solution.coffeeshop_order_management.service.VoucherService;
import com.architecture_solution.coffeeshop_order_management.util.OrderPricing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;
    private final VoucherService voucherService;
    @Value("${app.pricing.tax-rate:0}")
    private BigDecimal taxRate;


    private Order findOrderFor(String orderId, String userId, boolean admin) {
        Optional<Order> order = admin
                ? orderRepository.findWithItemsById(orderId)
                : orderRepository.findByIdAndUserId(orderId, userId);
        return order.orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

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

    private Map<String, MenuItem> loadMenuItems(CreateOrderRequest request) {
        List<String> ids = request.getItems().stream().map(OrderItemRequest::getMenuItemId).toList();
        return menuItemRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));
    }

    private List<OrderItem> buildOrderItems(CreateOrderRequest request, Map<String, MenuItem> menuItems) {
        return request.getItems().stream().map(line -> {
            MenuItem menuItem = menuItems.get(line.getMenuItemId());
            if (menuItem == null) {
                throw new AppException(ErrorCode.MENU_ITEM_NOT_FOUND);
            }
            return buildOrderItem(line, menuItem);
        }).toList();
    }

    @Override
    @Transactional
    public OrderResponse createOrder(String userId, CreateOrderRequest request) {
        Map<String, MenuItem> menuItems = loadMenuItems(request);
        List<OrderItem> items = buildOrderItems(request, menuItems);

        BigDecimal subtotal = items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Voucher voucher = (request.getVoucherCode() == null || request.getVoucherCode().isBlank())
                ? null
                : voucherService.getValidVoucher(request.getVoucherCode());
        Integer discountPercent = voucher == null ? null : voucher.getDiscountPercent();
        OrderPricing.OrderTotals totals = OrderPricing.calculate(subtotal, discountPercent, taxRate);

        Order order = Order.builder().userId(userId).note(request.getNote())
                .voucherId(voucher == null ? null : voucher.getId())
                .subtotal(totals.subtotal())
                .discountAmount(totals.discountAmount())
                .taxAmount(totals.taxAmount())
                .totalAmount(totals.totalAmount())
                .build();
        items.forEach(order::addItem);

        return orderMapper.toResponse(orderRepository.save(order));
    }



    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(String orderId, String userId, boolean admin) {
        Order order = findOrderFor(orderId, userId, admin);
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public BillResponse getBill(String orderId, String userId, boolean admin) {
        Order order = findOrderFor(orderId, userId, admin);
        return orderMapper.toBill(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<List<OrderSummaryResponse>> getOrders(String userId, boolean admin, OrderStatus status,
                                                              String userIdFilter, LocalDate from, LocalDate to,
                                                              Pageable pageable) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new AppException(ErrorCode.VALIDATION_FAILED);
        }
        String ownerId = admin ? userIdFilter : userId;
        return toPageResponse(orderRepository.findAll(
                OrderSpecifications.search(status, ownerId, from, to), pageable));
    }

    private PageResponse<List<OrderSummaryResponse>> toPageResponse(Page<Order> page) {
        return PageResponse.<List<OrderSummaryResponse>>builder()
                .pageNum(page.getNumber())
                .pageSize(page.getSize())
                .totalPage(page.getTotalPages())
                .items(page.getContent().stream().map(orderMapper::toSummary).toList())
                .build();
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(String orderId, CancelOrderRequest request, String userId, boolean admin) {
        Order order = findOrderFor(orderId, userId, admin);
        order.cancel(request == null ? null : request.getCancelReason());
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse confirmOrder(String orderId) {
        Order order = orderRepository.findWithItemsById(orderId).
                orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.confirm();
        return orderMapper.toResponse(order);
    }
}
