package com.architecture_solution.coffeeshop_order_management.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class OrderPricing {
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private OrderPricing() {
    }

    public static BigDecimal lineTotal(BigDecimal unitPrice, int quantity) {
        return round(unitPrice.multiply(BigDecimal.valueOf(quantity)));
    }

    /**
     * discountPercent may be null when no voucher is applied.
     */
    public static OrderTotals calculate(BigDecimal subtotal, Integer discountPercent, BigDecimal taxRate) {
        BigDecimal roundedSubtotal = round(subtotal);
        BigDecimal discount = discountPercent == null
                ? round(BigDecimal.ZERO)
                : round(roundedSubtotal.multiply(BigDecimal.valueOf(discountPercent)).divide(HUNDRED));
        BigDecimal tax = round(roundedSubtotal.subtract(discount).multiply(taxRate));
        BigDecimal total = roundedSubtotal.subtract(discount).add(tax);
        return new OrderTotals(roundedSubtotal, discount, tax, total);
    }

    private static BigDecimal round(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public record OrderTotals(BigDecimal subtotal, BigDecimal discountAmount,
                              BigDecimal taxAmount, BigDecimal totalAmount) {
    }
}
