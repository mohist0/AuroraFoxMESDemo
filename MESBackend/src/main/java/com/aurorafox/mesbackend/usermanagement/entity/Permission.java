package com.aurorafox.mesbackend.usermanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 权限信息表实体类，对应 permissions 表
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    /** 权限编号，主键 */
    @Id
    @Column(name = "permission_id")
    private String permissionId;

    /** 权限名称 */
    @Column(name = "permission_name")
    private String permissionName;

    /** 权限类型 */
    @Column(name = "permission_type")
    private String permissionType;

    /** 权限说明 */
    @Column(name = "permission_desc")
    private String permissionDesc;

    /** 创建时间 */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
