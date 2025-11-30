package com.aurorafox.mesbackend.auth.dto;

import lombok.Data;

/**
 * LoginResponseDto
 * ----------------------------
 * 登录响应 DTO：
 *  - 返回 JWT Token
 *  - 返回当前登录角色信息
 */
@Data
public class LoginResponseDto {
    private String token;    // JWT Token
    private String type = "Bearer"; // Token 类型前缀
    private String roleId;   // 当前登录角色ID
    private String roleName; // 当前登录角色名称（便于前端展示）
}
