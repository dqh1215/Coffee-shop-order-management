package com.architecture_solution.coffeeshop_order_management.controller;


import com.architecture_solution.coffeeshop_order_management.entity.MenuItem;
import com.architecture_solution.coffeeshop_order_management.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {
    private final MenuItemService menuItemService;

    @GetMapping
    public List<MenuItem> getAll() {
        return menuItemService.getAllMenuItems();
    }
}
