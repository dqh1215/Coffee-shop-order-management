package com.architecture_solution.coffeeshop_order_management.controller;

import com.architecture_solution.coffeeshop_order_management.config.openapi.BadRequestResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.CreatedResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.ForbiddenResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.NotFoundResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.OkResponse;
import com.architecture_solution.coffeeshop_order_management.dto.common.ApiResponse;
import com.architecture_solution.coffeeshop_order_management.dto.common.PageResponse;
import com.architecture_solution.coffeeshop_order_management.dto.menu.request.MenuItemRequest;
import com.architecture_solution.coffeeshop_order_management.dto.menu.response.MenuItemResponse;
import com.architecture_solution.coffeeshop_order_management.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Menu item", description = "Xem menu (public) và quản lý menu (ADMIN)")
@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {
    private static final int MAX_PAGE_SIZE = 100;

    private final MenuItemService menuItemService;

    @Operation(summary = "Tìm kiếm món",
            description = "Public. Chỉ trả về món đang hoạt động, sắp xếp theo tên. Món available = false vẫn được liệt kê nhưng không đặt được.")
    @SecurityRequirements
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<List<MenuItemResponse>>>> getAll(
            @Parameter(description = "Lọc theo danh mục")
            @RequestParam(name = "category_id", required = false) String categoryId,
            @Parameter(description = "Tìm theo tên món")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "Số trang, bắt đầu từ 0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số phần tử mỗi trang (1-100)")
            @RequestParam(defaultValue = "10") int size) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("name").ascending());

        return ResponseEntity.ok(ApiResponse.success(
                menuItemService.search(categoryId, keyword, pageable)));
    }

    @Operation(summary = "Xem chi tiết món", description = "Public.")
    @OkResponse
    @SecurityRequirements
    @NotFoundResponse
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getById(id)));
    }

    @Operation(summary = "Thêm món", description = "ADMIN. 404 nếu category_id không tồn tại.")
    @CreatedResponse
    @BadRequestResponse
    @ForbiddenResponse
    @NotFoundResponse
    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemResponse>> create(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(menuItemService.create(request), "Menu item created successfully"));
    }

    @Operation(summary = "Cập nhật món", description = "ADMIN. Giá mới không ảnh hưởng tới các đơn đã đặt.")
    @OkResponse
    @BadRequestResponse
    @ForbiddenResponse
    @NotFoundResponse
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                menuItemService.update(id, request), "Menu item updated successfully"));
    }

    @Operation(summary = "Xoá món", description = "ADMIN. Xoá mềm (is_active = false), không xoá khỏi database.")
    @OkResponse
    @ForbiddenResponse
    @NotFoundResponse
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        menuItemService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Menu item deleted successfully"));
    }
}