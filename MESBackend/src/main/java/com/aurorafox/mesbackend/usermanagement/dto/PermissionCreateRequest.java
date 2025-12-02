package com.aurorafox.mesbackend.usermanagement.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 新增权限请求体
 */
@Data
public class PermissionCreateRequest {
    @NotBlank(message = "权限编号不能为空")
    private String permissionId;

    @NotBlank(message = "权限名称不能为空")
    private String permissionName;

    private String permissionDesc;
}

