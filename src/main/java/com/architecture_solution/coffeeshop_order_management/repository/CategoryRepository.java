package com.architecture_solution.coffeeshop_order_management.repository;

import com.architecture_solution.coffeeshop_order_management.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findAllByActiveTrueOrderByNameAsc();

    boolean existsByIdAndActiveTrue(String id);
}