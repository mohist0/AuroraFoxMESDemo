package com.aurorafox.mesbackend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebCorsConfig
 * ----------------------------
 * 全局跨域配置：
 *  - 允许同一局域网内所有电脑访问后端接口
 *  - 支持常用的 HTTP 方法
 *  - 允许携带认证信息（如 JWT Token）
 */
@Configuration
public class WebCorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 匹配所有接口
                        .allowedOriginPatterns("*") // 允许所有来源（局域网内所有电脑）
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // 允许所有请求头
                        .allowCredentials(true) // 允许携带 Cookie 或认证信息
                        .maxAge(3600); // 预检请求缓存时间（秒）
            }
        };
    }
}
