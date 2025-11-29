package com.aurorafox.mesbackend.usermanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 角色信息表实体类，对应 roles 表
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    /** 角色编号，主键 */
    @Id
    @Column(name = "role_id")
    private String roleId;

    /** 角色名称 */
    @Column(name = "role_name")
    private String roleName;

    /** 角色说明 */
    @Column(name = "role_desc")
    private String roleDesc;

    /** 创建时间 */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
