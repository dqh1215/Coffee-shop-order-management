package com.architecture_solution.coffeeshop_order_management.config;

import com.architecture_solution.coffeeshop_order_management.filter.JwtAuthFilter;
import com.architecture_solution.coffeeshop_order_management.security.CustomAccessDeniedHandler;
import com.architecture_solution.coffeeshop_order_management.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**", "/api/menu-items/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/menu-items").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/menu-items/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/menu-items/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/orders/*/confirm").hasRole("ADMIN")
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/vouchers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/vouchers/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider authenticationProvider) {

        return new ProviderManager(authenticationProvider);
    }
}
