package com.aurorafox.mesbackend.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 配置类
 * 作用：
 * 1. 启用 Springdoc OpenAPI（Swagger UI）功能，自动扫描项目中的 REST 接口。
 * 2. 在 Swagger UI 页面展示接口文档，方便前后端开发人员调试和对接。
 * 3. 定义全局的 API 信息（标题、版本、描述等）。
 * 访问路径：
 * - Swagger UI: http://localhost:8080/swagger-ui/index.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 */
@Configuration // 声明这是一个配置类，会被 Spring Boot 自动加载
public class SwaggerConfig {

    /**
     * 定义 OpenAPI Bean，用于配置接口文档的基本信息
     *
     * @return OpenAPI 对象，包含标题、版本、描述等信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MES Backend API")          // 文档标题
                        .version("1.0")                    // 文档版本
                        .description("后端接口文档")); // 文档描述
    }
}
