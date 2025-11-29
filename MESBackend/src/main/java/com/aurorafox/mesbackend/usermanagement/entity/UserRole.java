package com.aurorafox.mesbackend.usermanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 用户角色关系表实体类，对应 user_role 表
 */
@Entity
@Table(name = "user_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

    /** 数据编号，主键 */
    @Id
    @Column(name = "ur_id")
    private String urId;

    /** 用户编号，外键关联 users.user_id */
    @Column(name = "user_id")
    private String userId;

    /** 角色编号，外键关联 roles.role_id */
    @Column(name = "role_id")
    private String roleId;

    /** 创建时间 */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
