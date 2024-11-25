package com.poly.shoptrangsuc.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {

    @Bean
    public com.cloudinary.Cloudinary configKey() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dxk3vxdzj");  // Your Cloudinary cloud name
        config.put("api_key", "617575634416996");  // Your Cloudinary API key
        config.put("api_secret", "7D4dmSoHNz_YWM0PvBqs9aep-_c");  // Your Cloudinary API secret
        return new com.cloudinary.Cloudinary(config);
    }
}
