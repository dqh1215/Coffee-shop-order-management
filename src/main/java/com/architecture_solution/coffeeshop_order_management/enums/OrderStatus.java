package com.architecture_solution.coffeeshop_order_management.enums;

public enum OrderStatus {
    PENDING, CONFIRMED, CANCELLED;

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case PENDING   -> next == CONFIRMED || next == CANCELLED;
            case CONFIRMED -> next == CANCELLED;
            case CANCELLED -> false;
        };
    }
}
