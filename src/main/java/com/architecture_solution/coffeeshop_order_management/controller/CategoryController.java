package com.architecture_solution.coffeeshop_order_management.controller;

import com.architecture_solution.coffeeshop_order_management.dto.common.ApiResponse;
import com.architecture_solution.coffeeshop_order_management.dto.menu.response.CategoryResponse;
import com.architecture_solution.coffeeshop_order_management.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getAll()));
    }
}