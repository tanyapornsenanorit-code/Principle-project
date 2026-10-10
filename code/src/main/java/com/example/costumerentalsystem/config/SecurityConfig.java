package com.example.costumerentalsystem.config;

import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.domain.enums.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
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
            // ต้องตรวจและปรับ CSRF สำหรับฟอร์ม Thymeleaf ก่อนเปิด Production
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // หน้าและทรัพยากรสาธารณะ
                .requestMatchers(
                    "/", "/login", "/register", "/logout",
                    "/forgot-password", "/forgot-password/**",
                    "/reset-password", "/reset-password/**",
                    "/css/**", "/js/**", "/images/**", "/uploads/**",
                    "/webjars/**", "/favicon.ico", "/error",
                    "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
                ).permitAll()

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ต้องเป็น ADMIN
                .requestMatchers(
                    "/admin/**",
                    "/api/v1/shipments/**",
                    "/costumes/admin/**",
                    "/costumes/edit/**",
                    "/costumes/update/**",
                    "/costumes/delete/**"
                ).access((authentication, context) ->
                    new AuthorizationDecision(isAdmin(context.getRequest()))
                )

                // ผู้ใช้ดูประวัติของตนเองได้; ADMIN ดูได้ทุกคน
                .requestMatchers("/api/v1/users/**").access(
                    (authentication, context) ->
                        new AuthorizationDecision(
                            canReadUserResource(context.getRequest())
                        )
                )

                // อ่าน Costume และ Category ได้โดยไม่ต้อง Login
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/costumes", "/api/v1/costumes/**",
                    "/api/v1/categories", "/api/v1/categories/**"
                ).permitAll()

                // การเปลี่ยนแปลงข้อมูล API ต้องเป็น ADMIN
                .requestMatchers(
                    "/api/v1/costumes", "/api/v1/costumes/**",
                    "/api/v1/categories", "/api/v1/categories/**"
                ).access((authentication, context) ->
                    new AuthorizationDecision(isAdmin(context.getRequest()))
                )

                // หน้าอ่านชุดของเว็บไซต์เปิดให้ Guest ได้
                .requestMatchers(HttpMethod.GET, "/costumes", "/costumes/**").permitAll()

                // เส้นทางแก้ไขชุดที่ไม่ได้ระบุด้านบนยังต้องเป็น ADMIN
                .requestMatchers("/costumes", "/costumes/**").access(
                    (authentication, context) ->
                        new AuthorizationDecision(isAdmin(context.getRequest()))
                )

                // เส้นทางบัญชีและรายการเช่าต้องมี Session
                .requestMatchers(
                    "/rentals", "/rentals/**",
                    "/orders", "/orders/**",
                    "/payments", "/payments/**",
                    "/user", "/user/**"
                ).access((authentication, context) ->
                    new AuthorizationDecision(
                        getSessionUser(context.getRequest()) != null
                    )
                )

                // ปริยาย: Request อื่นต้องมี Session; ไม่อนุญาตทุกคนโดยอัตโนมัติ
                .anyRequest().access((authentication, context) ->
                    new AuthorizationDecision(
                        getSessionUser(context.getRequest()) != null
                    )
                )
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler((request, response, exception) -> {
                    User user = getSessionUser(request);

                    if (user == null) {
                        if (request.getRequestURI().startsWith("/api/")) {
                            response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                "Authentication required"
                            );
                        } else {
                            response.sendRedirect("/login");
                        }
                    } else {
                        response.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "Access denied"
                        );
                    }
                })
            );

        return http.build();
    }

    private static User getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Object value = session.getAttribute("loggedInUser");
        return value instanceof User ? (User) value : null;
    }

    private static boolean isAdmin(HttpServletRequest request) {
        User user = getSessionUser(request);
        return user != null && user.getRole() == Role.ADMIN;
    }

    private static boolean canReadUserResource(HttpServletRequest request) {
        User user = getSessionUser(request);
        if (user == null) {
            return false;
        }

        if (user.getRole() == Role.ADMIN) {
            return true;
        }

        // URI รูปแบบ /api/v1/users/{id}/rentals
        String[] segments = request.getRequestURI().split("/");
        if (segments.length < 6 || user.getId() == null) {
            return false;
        }

        try {
            long requestedUserId = Long.parseLong(segments[4]);
            return user.getId().longValue() == requestedUserId;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
