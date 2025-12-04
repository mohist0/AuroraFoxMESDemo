package com.aurorafox.mesbackend.auth.dto;

import lombok.Data;
import java.util.List;

/**
 * LoginResponseDto
 * ----------------------------
 * 登录响应 DTO：
 *  - 返回 JWT Token
 *  - 返回当前登录角色信息
 *  - 返回当前角色对应的权限列表
 */
@Data
public class LoginResponseDto {
    private String token;       // JWT Token
    private String type = "Bearer"; // Token 类型前缀
    private String roleId;      // 当前登录角色ID
    private String roleName;    // 当前登录角色名称
    private List<String> permissions; // 当前角色对应的权限列表
}
