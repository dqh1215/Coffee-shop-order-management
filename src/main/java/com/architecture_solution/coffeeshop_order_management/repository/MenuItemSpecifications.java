package com.architecture_solution.coffeeshop_order_management.repository;

import com.architecture_solution.coffeeshop_order_management.entity.MenuItem;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class MenuItemSpecifications {
    private MenuItemSpecifications() {
    }

    /**
     * Chỉ lấy món chưa bị xóa mềm; lọc thêm theo danh mục và từ khóa tên nếu có.
     */
    public static Specification<MenuItem> search(String categoryId, String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("active")));

            if (categoryId != null && !categoryId.isBlank()) {
                predicates.add(cb.equal(root.get("categoryId"), categoryId.trim()));
            }
            if (keyword != null && !keyword.isBlank()) {
                String escaped = keyword.trim().toLowerCase(Locale.ROOT)
                        .replace("\\", "\\\\")
                        .replace("%", "\\%")
                        .replace("_", "\\_");
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + escaped + "%", '\\'));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}