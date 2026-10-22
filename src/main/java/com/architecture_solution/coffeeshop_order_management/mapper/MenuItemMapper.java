package com.architecture_solution.coffeeshop_order_management.mapper;

import com.architecture_solution.coffeeshop_order_management.dto.menu.request.MenuItemRequest;
import com.architecture_solution.coffeeshop_order_management.dto.menu.response.MenuItemResponse;
import com.architecture_solution.coffeeshop_order_management.entity.MenuItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    MenuItemResponse toResponse(MenuItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "available", source = "available", defaultValue = "true")
    MenuItem toEntity(MenuItemRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(MenuItemRequest request, @MappingTarget MenuItem item);
}