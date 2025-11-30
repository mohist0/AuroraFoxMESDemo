package com.aurorafox.mesbackend.auth.dto;

import lombok.Data;

/**
 * LoginRequestDto
 * ----------------------------
 * 登录请求 DTO：
 *  - 包含用户名、密码和角色ID
 */
@Data
public class LoginRequestDto {
    private String username; // 用户名
    private String password; // 密码
    private String roleId;   // 登录时选择的角色ID
}
