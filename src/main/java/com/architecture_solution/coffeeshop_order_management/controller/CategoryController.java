package com.architecture_solution.coffeeshop_order_management.controller;

import com.architecture_solution.coffeeshop_order_management.dto.common.ApiResponse;
import com.architecture_solution.coffeeshop_order_management.dto.menu.response.CategoryResponse;
import com.architecture_solution.coffeeshop_order_management.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Category", description = "Danh mục món")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Danh sách danh mục đang hoạt động", description = "Public.")
    @SecurityRequirements
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getAll()));
    }
}