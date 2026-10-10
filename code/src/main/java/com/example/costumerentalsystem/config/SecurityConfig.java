package com.example.costumerentalsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ปิด CSRF ชั่วคราวเพื่อให้กดส่ง Form ต่างๆ ได้ง่าย
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 🔓 ปลดล็อกให้เข้าได้ทุก URL โดยไม่ต้องเข้าสู่ระบบ
            );

        return http.build();
    }
}