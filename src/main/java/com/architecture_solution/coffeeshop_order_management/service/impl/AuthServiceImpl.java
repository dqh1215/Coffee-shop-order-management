package com.architecture_solution.coffeeshop_order_management.service.impl;

import com.architecture_solution.coffeeshop_order_management.dto.auth.request.LoginRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.request.RegisterRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.response.LoginResponse;
import com.architecture_solution.coffeeshop_order_management.entity.User;
import com.architecture_solution.coffeeshop_order_management.exception.AppException;
import com.architecture_solution.coffeeshop_order_management.exception.ErrorCode;
import com.architecture_solution.coffeeshop_order_management.mapper.UserMapper;
import com.architecture_solution.coffeeshop_order_management.repository.UserRepository;
import com.architecture_solution.coffeeshop_order_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userMapper.toEntity(request);
        userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndActiveTrue(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if(!request.getPassword().equals(user.getPasswordHash())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        if(!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        return LoginResponse.builder().user(userMapper.toResponse(user)).build();
    }
}
