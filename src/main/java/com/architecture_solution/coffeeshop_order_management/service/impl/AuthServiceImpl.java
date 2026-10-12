package com.architecture_solution.coffeeshop_order_management.service.impl;

import com.architecture_solution.coffeeshop_order_management.dto.auth.request.LoginRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.request.RegisterRequest;
import com.architecture_solution.coffeeshop_order_management.dto.auth.response.LoginResponse;
import com.architecture_solution.coffeeshop_order_management.entity.User;
import com.architecture_solution.coffeeshop_order_management.exception.AppException;
import com.architecture_solution.coffeeshop_order_management.exception.ErrorCode;
import com.architecture_solution.coffeeshop_order_management.mapper.UserMapper;
import com.architecture_solution.coffeeshop_order_management.repository.UserRepository;
import com.architecture_solution.coffeeshop_order_management.security.CustomUserDetails;
import com.architecture_solution.coffeeshop_order_management.service.AuthService;
import com.architecture_solution.coffeeshop_order_management.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

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
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getPassword()
                            )
                    );

            CustomUserDetails userDetails =
                    (CustomUserDetails) authentication.getPrincipal();

            User user = userDetails.getUser();

            String token = jwtUtils.generateToken(user);
            Claims claims = jwtUtils.parseClaims(token);

            return LoginResponse.builder()
                    .accessToken(token)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .expiresAt(jwtUtils.extractExpiration(claims).toInstant())
                    .build();

        } catch (AuthenticationException ex) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
    }
}
