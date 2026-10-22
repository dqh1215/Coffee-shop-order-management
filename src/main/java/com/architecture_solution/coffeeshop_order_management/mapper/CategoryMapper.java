package com.architecture_solution.coffeeshop_order_management.mapper;

import com.architecture_solution.coffeeshop_order_management.dto.menu.response.CategoryResponse;
import com.architecture_solution.coffeeshop_order_management.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
}