package com.architecture_solution.coffeeshop_order_management.controller;

import com.architecture_solution.coffeeshop_order_management.dto.common.ApiResponse;
import com.architecture_solution.coffeeshop_order_management.dto.common.PageResponse;
import com.architecture_solution.coffeeshop_order_management.dto.menu.request.MenuItemRequest;
import com.architecture_solution.coffeeshop_order_management.dto.menu.response.MenuItemResponse;
import com.architecture_solution.coffeeshop_order_management.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {
    private static final int MAX_PAGE_SIZE = 100;

    private final MenuItemService menuItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<List<MenuItemResponse>>>> getAll(
            @RequestParam(name = "category_id", required = false) String categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("name").ascending());

        return ResponseEntity.ok(ApiResponse.success(
                menuItemService.search(categoryId, keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemResponse>> create(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(menuItemService.create(request), "Menu item created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                menuItemService.update(id, request), "Menu item updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        menuItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}