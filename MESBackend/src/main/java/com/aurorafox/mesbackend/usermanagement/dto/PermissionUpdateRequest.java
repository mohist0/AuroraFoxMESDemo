package com.aurorafox.mesbackend.usermanagement.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 更新权限请求体
 */
@Data
public class PermissionUpdateRequest {
    private String permissionId; // 路径参数传入，不允许修改

    @NotBlank(message = "权限名称不能为空")
    private String permissionName;

    private String permissionDesc;
}
