package com.aurorafox.mesbackend.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import lombok.Data;
import java.util.Set;

/**
 * 角色数据传输对象
 * 用于角色信息的传输和展示
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "角色信息传输对象")
public class RoleDto {

    /**
     * 角色编号
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色说明
     */
    private String roleDesc;

    /**
     * 创建时间（仅用于查询返回，创建时无需传入）
     */
    private LocalDateTime createTime;

    /**
     * 关联的权限ID集合（用于角色-权限关联操作）
     */
    private Set<String> permissionIds;

    /**
     * 关联的用户ID集合（用于查询角色关联的用户，创建/修改时无需传入）
     */
    private Set<String> userIds;
}