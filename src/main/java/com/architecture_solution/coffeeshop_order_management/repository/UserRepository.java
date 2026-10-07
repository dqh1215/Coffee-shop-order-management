package com.architecture_solution.coffeeshop_order_management.repository;

import com.architecture_solution.coffeeshop_order_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String username);

    Optional<User> findByEmail(String username);
    Optional<User> findByEmailAndActiveTrue(String username);
}
