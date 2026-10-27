package com.architecture_solution.coffeeshop_order_management.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @Test
    void pending_canBeConfirmedOrCancelled() {
        assertThat(OrderStatus.PENDING.canTransitionTo(OrderStatus.CONFIRMED)).isTrue();
        assertThat(OrderStatus.PENDING.canTransitionTo(OrderStatus.CANCELLED)).isTrue();
        assertThat(OrderStatus.PENDING.canTransitionTo(OrderStatus.PENDING)).isFalse();
    }

    @Test
    void confirmed_canBeCompletedOrCancelled() {
        assertThat(OrderStatus.CONFIRMED.canTransitionTo(OrderStatus.COMPLETED)).isTrue();
        assertThat(OrderStatus.CONFIRMED.canTransitionTo(OrderStatus.CANCELLED)).isTrue();
        assertThat(OrderStatus.CONFIRMED.canTransitionTo(OrderStatus.PENDING)).isFalse();
        assertThat(OrderStatus.CONFIRMED.canTransitionTo(OrderStatus.CONFIRMED)).isFalse();
    }

    @Test
    void completed_isFinal() {
        for (OrderStatus next : OrderStatus.values()) {
            assertThat(OrderStatus.COMPLETED.canTransitionTo(next)).isFalse();
        }
    }
}
