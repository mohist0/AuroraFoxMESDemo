package com.aurorafox.mesbackend.auth.dto;

import lombok.Data;

/**
 * LoginRequestDto
 * ----------------------------
 * 登录请求 DTO：
 *  - 包含用户名和密码
 */
@Data
public class LoginRequestDto {
    private String username; // 用户名
    private String password; // 密码
}
