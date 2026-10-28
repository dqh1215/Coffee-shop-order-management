package com.architecture_solution.coffeeshop_order_management.repository;

import com.architecture_solution.coffeeshop_order_management.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    @EntityGraph(attributePaths = "items")
    Optional<Order> findByIdAndUserId(String id, String userId);
    @EntityGraph(attributePaths = "items")
    Optional<Order> findWithItemsById(String id);
}