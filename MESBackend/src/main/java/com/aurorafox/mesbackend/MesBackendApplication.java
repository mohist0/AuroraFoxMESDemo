package com.aurorafox.mesbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目主入口类
 * 1. @SpringBootApplication 注解会开启 Spring Boot 的自动配置、组件扫描等功能。
 * 2. main 方法调用 SpringApplication.run(...)，启动内嵌的 Tomcat 并加载整个应用上下文。
 */
@SpringBootApplication
public class MesBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(MesBackendApplication.class, args);
    }
}
