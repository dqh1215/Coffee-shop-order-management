package com.architecture_solution.coffeeshop_order_management.service.impl;

import com.architecture_solution.coffeeshop_order_management.dto.menu.response.CategoryResponse;
import com.architecture_solution.coffeeshop_order_management.mapper.CategoryMapper;
import com.architecture_solution.coffeeshop_order_management.repository.CategoryRepository;
import com.architecture_solution.coffeeshop_order_management.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAllByActiveTrueOrderByNameAsc().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}