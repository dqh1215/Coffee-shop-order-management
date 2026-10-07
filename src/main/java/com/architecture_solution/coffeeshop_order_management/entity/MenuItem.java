package com.architecture_solution.coffeeshop_order_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "menu_items")
public class MenuItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "category_id")
    private String categoryId;

    private String name;

    private String description;

    private BigDecimal price;

    private boolean available = true;
}
