package com.aurorafox.mesbackend.usermanagement.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 用户信息返回 DTO
 * 用于返回给前端的用户数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;            // 用户编号
    private String userName;          // 用户名
    private LocalDateTime lastLoginTime; // 最近登录时间
    private LocalDateTime createTime;    // 创建时间
}
