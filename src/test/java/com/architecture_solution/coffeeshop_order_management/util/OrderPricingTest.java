package com.architecture_solution.coffeeshop_order_management.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPricingTest {

    @Test
    void lineTotal_multipliesUnitPriceByQuantity() {
        assertThat(OrderPricing.lineTotal(new BigDecimal("25000.00"), 3))
                .isEqualByComparingTo("75000.00");
    }

    @Test
    void calculate_withoutVoucher_hasNoDiscount() {
        OrderPricing.OrderTotals totals = OrderPricing.calculate(
                new BigDecimal("100000"), null, new BigDecimal("0.08"));

        assertThat(totals.subtotal()).isEqualTo(new BigDecimal("100000.00"));
        assertThat(totals.discountAmount()).isEqualTo(new BigDecimal("0.00"));
        assertThat(totals.taxAmount()).isEqualTo(new BigDecimal("8000.00"));
        assertThat(totals.totalAmount()).isEqualTo(new BigDecimal("108000.00"));
    }

    @Test
    void calculate_appliesTaxAfterDiscount() {
        OrderPricing.OrderTotals totals = OrderPricing.calculate(
                new BigDecimal("100000"), 10, new BigDecimal("0.08"));

        assertThat(totals.discountAmount()).isEqualTo(new BigDecimal("10000.00"));
        assertThat(totals.taxAmount()).isEqualTo(new BigDecimal("7200.00"));
        assertThat(totals.totalAmount()).isEqualTo(new BigDecimal("97200.00"));
    }

    @Test
    void calculate_roundsHalfUp() {
        OrderPricing.OrderTotals totals = OrderPricing.calculate(
                new BigDecimal("10.05"), 10, new BigDecimal("0.1"));

        // discount 1.005 -> 1.01, tax (10.05 - 1.01) * 0.1 = 0.904 -> 0.90
        assertThat(totals.discountAmount()).isEqualTo(new BigDecimal("1.01"));
        assertThat(totals.taxAmount()).isEqualTo(new BigDecimal("0.90"));
        assertThat(totals.totalAmount()).isEqualTo(new BigDecimal("9.94"));
    }

    @Test
    void calculate_withZeroTaxRate_totalEqualsSubtotalMinusDiscount() {
        OrderPricing.OrderTotals totals = OrderPricing.calculate(
                new BigDecimal("50000"), 100, BigDecimal.ZERO);

        assertThat(totals.discountAmount()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(totals.taxAmount()).isEqualTo(new BigDecimal("0.00"));
        assertThat(totals.totalAmount()).isEqualTo(new BigDecimal("0.00"));
    }
}
