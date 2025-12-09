package com.aurorafox.mesbackend.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色详情数据传输对象
 * 包含角色详细信息及关联的用户和权限信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "角色详情信息传输对象")
public class RoleDetailDto {

    /**
     * 角色编号
     */
    @Schema(description = "角色编号", example = "ROLE_ADMIN")
    private String roleId;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;

    /**
     * 角色说明
     */
    @Schema(description = "角色说明", example = "拥有系统所有权限")
    private String roleDesc;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 关联用户信息列表
     */
    @Schema(description = "关联用户信息列表")
    private List<SimpleUserDto> users;

    /**
     * 关联权限信息列表
     */
    @Schema(description = "关联权限信息列表")
    private List<SimplePermissionDto> permissions;
}

/**
 * 简化用户信息传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "简化用户信息")
class SimpleUserDto {
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "姓名", example = "张三")
    private String realName;
}

/**
 * 简化权限信息传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "简化权限信息")
class SimplePermissionDto {
    @Schema(description = "权限ID", example = "1")
    private Long permissionId;

    @Schema(description = "权限编码", example = "USER:VIEW")
    private String permissionCode;

    @Schema(description = "权限名称", example = "查看用户")
    private String permissionName;
}