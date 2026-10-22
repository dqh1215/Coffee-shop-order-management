package com.architecture_solution.coffeeshop_order_management.service.impl;

import com.architecture_solution.coffeeshop_order_management.dto.common.PageResponse;
import com.architecture_solution.coffeeshop_order_management.dto.menu.request.MenuItemRequest;
import com.architecture_solution.coffeeshop_order_management.dto.menu.response.MenuItemResponse;
import com.architecture_solution.coffeeshop_order_management.entity.MenuItem;
import com.architecture_solution.coffeeshop_order_management.exception.AppException;
import com.architecture_solution.coffeeshop_order_management.exception.ErrorCode;
import com.architecture_solution.coffeeshop_order_management.mapper.MenuItemMapper;
import com.architecture_solution.coffeeshop_order_management.repository.CategoryRepository;
import com.architecture_solution.coffeeshop_order_management.repository.MenuItemRepository;
import com.architecture_solution.coffeeshop_order_management.repository.MenuItemSpecifications;
import com.architecture_solution.coffeeshop_order_management.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemMapper menuItemMapper;

    @Override
    public PageResponse<List<MenuItemResponse>> search(String categoryId, String keyword, Pageable pageable) {
        Page<MenuItemResponse> page = menuItemRepository
                .findAll(MenuItemSpecifications.search(categoryId, keyword), pageable)
                .map(menuItemMapper::toResponse);

        return PageResponse.<List<MenuItemResponse>>builder()
                .pageNum(page.getNumber())
                .pageSize(page.getSize())
                .totalPage(page.getTotalPages())
                .items(page.getContent())
                .build();
    }

    @Override
    public MenuItemResponse getById(String id) {
        return menuItemMapper.toResponse(findActive(id));
    }

    @Override
    @Transactional
    public MenuItemResponse create(MenuItemRequest request) {
        requireActiveCategory(request.getCategoryId());

        // toEntity mặc định available = true nếu request không gửi
        MenuItem item = menuItemMapper.toEntity(request);
        item.setName(item.getName().trim());
        return menuItemMapper.toResponse(menuItemRepository.save(item));
    }

    @Override
    @Transactional
    public MenuItemResponse update(String id, MenuItemRequest request) {
        MenuItem item = findActive(id);
        requireActiveCategory(request.getCategoryId());

        // updateEntity dùng NullValuePropertyMappingStrategy.IGNORE
        // → available = null sẽ giữ nguyên giá trị cũ (đúng nghiệp vụ).
        menuItemMapper.updateEntity(request, item);
        item.setName(item.getName().trim());
        return menuItemMapper.toResponse(item);
    }

    /**
     * Xóa mềm: giữ lại bản ghi để không làm sai lịch sử đơn hàng.
     */
    @Override
    @Transactional
    public void delete(String id) {
        findActive(id).setActive(false);
    }

    private MenuItem findActive(String id) {
        return menuItemRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new AppException(ErrorCode.MENU_ITEM_NOT_FOUND));
    }

    private void requireActiveCategory(String categoryId) {
        if (!categoryRepository.existsByIdAndActiveTrue(categoryId)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }
}