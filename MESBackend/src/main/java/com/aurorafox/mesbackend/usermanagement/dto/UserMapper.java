package com.aurorafox.mesbackend.usermanagement.dto;

import com.aurorafox.mesbackend.usermanagement.entity.User;

public class UserMapper {

    // Entity → DTO
    public static UserDto toDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .userId(user.getUserId())
                .userType(user.getUserType())
                .userName(user.getUserName())
                .lastLoginTime(user.getLastLoginTime())
                .createTime(user.getCreateTime())
                .build();
    }

    // DTO → Entity (仅用于创建用户时)
//    public static User toEntity(UserDto dto, String password, String salt) {
//        if (dto == null) return null;
//        return User.builder()
//                .userId(dto.getUserId())
//                .userType(dto.getUserType())
//                .userName(dto.getUserName())
//                .userPassword(password)
//                .saltValue(salt)
//                .lastLoginTime(dto.getLastLoginTime())
//                .createTime(dto.getCreateTime())
//                .build();
//    }
}
