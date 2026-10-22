package com.architecture_solution.coffeeshop_order_management.entity;

import com.architecture_solution.coffeeshop_order_management.enums.OrderStatus;
import com.architecture_solution.coffeeshop_order_management.exception.AppException;
import com.architecture_solution.coffeeshop_order_management.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.math.BigDecimal.ZERO;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "voucher_id", length = 36)
    private String voucherId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)

    @Builder.Default
    private BigDecimal discountAmount = ZERO.setScale(2, RoundingMode.UNNECESSARY);

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = ZERO.setScale(2, RoundingMode.UNNECESSARY);

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 255)
    private String note;

    @Column(name = "cancel_reason", length = 255)
    private String cancelReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void changeStatus(OrderStatus next) {
        if (!orderStatus.canTransitionTo(next)) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }
        this.orderStatus = next;
    }

    public void cancel(String reason) {
        changeStatus(OrderStatus.CANCELLED);
        this.cancelReason = reason;
    }

    public void confirm() {
        changeStatus(OrderStatus.CONFIRMED);
    }

    public void complete() {
        changeStatus(OrderStatus.COMPLETED);
    }
}