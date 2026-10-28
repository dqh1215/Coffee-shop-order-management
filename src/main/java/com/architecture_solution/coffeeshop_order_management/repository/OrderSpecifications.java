package com.architecture_solution.coffeeshop_order_management.repository;

import com.architecture_solution.coffeeshop_order_management.entity.Order;
import com.architecture_solution.coffeeshop_order_management.enums.OrderStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class OrderSpecifications {
    private OrderSpecifications() {}

    public static Specification<Order> search(OrderStatus status, String userId,
                                              LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(cb.equal(root.get("orderStatus"), status));
            }
            if (userId != null && !userId.isBlank()) {
                predicates.add(cb.equal(root.get("userId"), userId.trim()));
            }
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.<LocalDateTime>get("createdAt"), from.atStartOfDay()));
            }
            if (to != null) {
                predicates.add(cb.lessThan(
                        root.<LocalDateTime>get("createdAt"), to.plusDays(1).atStartOfDay()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
