package com.poly.shoptrangsuc.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // Cho phép tất cả các endpoint
            .allowedOrigins("http://localhost:3000") // Cho phép địa chỉ này
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Phương thức cho phép
            .allowedHeaders("*") // Cho phép tất cả headers
            .allowCredentials(true); // Cho phép gửi credentials như cookie // Cho phép gửi cookie
}
}
