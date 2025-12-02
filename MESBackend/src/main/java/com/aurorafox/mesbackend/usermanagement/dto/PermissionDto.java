package com.aurorafox.mesbackend.usermanagement.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 权限 DTO，用于接口层返回权限信息
 */
@Data
@Builder
public class PermissionDto {
    private String permissionId;   // 权限编号
    private String permissionName; // 权限名称
    private String permissionDesc; // 权限说明
    private LocalDateTime createTime; // 创建时间
}
