package com.architecture_solution.coffeeshop_order_management.service.impl;

import com.architecture_solution.coffeeshop_order_management.entity.MenuItem;
import com.architecture_solution.coffeeshop_order_management.repository.MenuItemRepository;
import com.architecture_solution.coffeeshop_order_management.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }
}
