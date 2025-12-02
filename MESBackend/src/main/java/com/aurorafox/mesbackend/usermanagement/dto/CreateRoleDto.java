package com.aurorafox.mesbackend.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 创建角色数据传输对象
 * 用于接收创建角色的请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "创建角色请求参数")
public class CreateRoleDto {

    /**
     * 角色编号
     */
    @NotBlank(message = "角色编号不能为空")
    @Pattern(regexp = "^[A-Z_]{3,50}$", message = "角色编号格式不正确，只能包含大写字母和下划线，长度3-50")
    @Schema(description = "角色编号", example = "ROLE_ADMIN", required = true)
    private String roleId;

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