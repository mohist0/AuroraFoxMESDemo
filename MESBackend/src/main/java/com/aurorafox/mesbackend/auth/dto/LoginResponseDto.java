package com.aurorafox.mesbackend.auth.dto;

import lombok.Data;

/**
 * LoginResponseDto
 * ----------------------------
 * 登录响应 DTO：
 *  - 返回 JWT Token
 *  - 默认类型为 Bearer
 */
@Data
public class LoginResponseDto {
    private String token;   // JWT Token
    private String type = "Bearer"; // Token 类型前缀
}
