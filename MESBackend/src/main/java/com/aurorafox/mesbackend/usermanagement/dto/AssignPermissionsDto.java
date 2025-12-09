package com.aurorafox.mesbackend.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * 分配权限数据传输对象
 * 用于接收为角色分配权限的请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "分配权限请求参数")
public class AssignPermissionsDto {

    /**
     * 权限ID列表
     */
    @Schema(description = "权限ID列表", required = true)
    private List<Long> permissionIds;
}