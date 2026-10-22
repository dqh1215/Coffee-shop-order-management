package com.architecture_solution.coffeeshop_order_management.service;

import com.architecture_solution.coffeeshop_order_management.dto.common.PageResponse;
import com.architecture_solution.coffeeshop_order_management.dto.menu.request.MenuItemRequest;
import com.architecture_solution.coffeeshop_order_management.dto.menu.response.MenuItemResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuItemService {
    PageResponse<List<MenuItemResponse>> search(String categoryId, String keyword, Pageable pageable);

    MenuItemResponse getById(String id);

    MenuItemResponse create(MenuItemRequest request);

    MenuItemResponse update(String id, MenuItemRequest request);

    void delete(String id);
}