package com.architecture_solution.coffeeshop_order_management.mapper;

import com.architecture_solution.coffeeshop_order_management.dto.auth.Request.RegisterRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.Response.UserResponse;
import com.architecture_solution.coffeeshop_order_management.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "role", ignore = true)
    User toEntity(RegisterRequest request);

    UserResponse toResponse(User user);
}
