package com.aurorafox.mesbackend.usermanagement.dto;

import com.aurorafox.mesbackend.usermanagement.entity.User;

/**
 * 用户实体与 DTO 的转换工具类
 */
public class UserMapper {

    // Entity → DTO
    public static UserDto toDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .lastLoginTime(user.getLastLoginTime())
                .createTime(user.getCreateTime())
                .build();
    }
}
