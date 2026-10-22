package com.architecture_solution.coffeeshop_order_management.config;

import com.architecture_solution.coffeeshop_order_management.entity.User;
import com.architecture_solution.coffeeshop_order_management.enums.RoleName;
import com.architecture_solution.coffeeshop_order_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
 * Tạo tài khoản ADMIN đầu tiên khi khởi động, lấy email/password từ biến môi trường.
 * Đã có user với email này thì bỏ qua.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        userRepository.save(User.builder()
                .email(adminEmail)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .fullName("Administrator")
                .role(RoleName.ADMIN)
                .build());
        log.info("Created default admin account: {}", adminEmail);
    }
}
