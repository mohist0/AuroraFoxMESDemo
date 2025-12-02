package com.aurorafox.mesbackend.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 更新角色数据传输对象
 * 用于接收更新角色的请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "更新角色请求参数")
public class UpdateRoleDto {

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 100, message = "角色名称长度必须在2-100之间")
    @Schema(description = "角色名称", example = "系统管理员", required = true)
    private String roleName;

    /**
     * 角色说明
     */
    @Size(max = 500, message = "角色说明长度不能超过500")
    @Schema(description = "角色说明", example = "拥有系统所有权限")
    private String roleDesc;
}