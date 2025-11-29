package com.aurorafox.mesbackend.usermanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 角色权限关系表实体类，对应 role_permission 表
 */
@Entity
@Table(name = "role_permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    /** 数据编号，主键 */
    @Id
    @Column(name = "rp_id")
    private String rpId;

    /** 角色编号，外键关联 roles.role_id */
    @Column(name = "role_id")
    private String roleId;

    /** 权限编号，外键关联 permissions.permission_id */
    @Column(name = "permission_id")
    private String permissionId;

    /** 创建时间 */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
