package com.creative.mart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://10.0.2.2:8080") // 에뮬레이터 요청 origin
            .allowedMethods("*")
            .allowCredentials(true); // 쿠키 허용
    }
}
