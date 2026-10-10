package com.example.costumerentalsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AdminAccessInterceptor adminAccessInterceptor;

    public WebConfig(AdminAccessInterceptor adminAccessInterceptor) {
        this.adminAccessInterceptor = adminAccessInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ชี้ไปที่โฟลเดอร์ static/uploads/ ในโปรเจกต์โดยตรง
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("classpath:/static/uploads/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAccessInterceptor)
                .addPathPatterns("/admin/**",
                        "/costumes/admin/**", "/costumes/edit/**",
                        "/costumes/update/**", "/costumes/delete/**",
                        "/api/v1/shipments/**");
    }
}
