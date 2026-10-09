package com.example.costumerentalsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String uploadDir;

    public WebConfig(@Value("${app.upload-dir:src/main/resources/static/uploads}") String uploadDir) {
        this.uploadDir = uploadDir.endsWith("/") ? uploadDir : uploadDir + "/";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1) โฟลเดอร์อัปโหลดจริงบนดิสก์/Volume (รูปที่ admin อัปโหลดหลัง deploy)
        // 2) classpath:/static/uploads/ คือรูปตัวอย่างที่ commit มากับโปรเจกต์ (อยู่ใน jar)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir, "classpath:/static/uploads/");
    }
}
