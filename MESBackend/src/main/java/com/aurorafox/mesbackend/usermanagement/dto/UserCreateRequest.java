package com.aurorafox.mesbackend.usermanagement.dto;

import lombok.*;

import java.util.List;

/**
 * 用户创建请求体 DTO
 * 用于接收前端传入的用户数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    private String userId;     // 用户编号
    private String userName;   // 用户名
    private String password;   // 明文密码
    private List<String> roleIds; // 绑定的角色ID列表
}

