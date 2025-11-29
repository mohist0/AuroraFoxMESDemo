package com.aurorafox.mesbackend.usermanagement.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    private String userId;
    private String userType;
    private String userName;
    private String password;  // 明文密码
}
