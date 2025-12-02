package com.aurorafox.mesbackend.usermanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    /**
     * 角色编号（主键）
     */
    @Id
    @Column(name = "role_id", length = 50, nullable = false)
    private String roleId;

    /**
     * 角色名称（非空）
     */
    @Column(name = "role_name", length = 100, nullable = false)
    private String roleName;

    /**
     * 角色说明
     */
    @Column(name = "role_desc", columnDefinition = "TEXT")
    private String roleDesc;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 角色与用户的多对多关联（通过中间表 user_role）
     *  cascade = CascadeType.REFRESH：刷新关联数据，不级联增删改
     */
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",  // 中间表名称
            joinColumns = @JoinColumn(name = "role_id"),  // 当前表在中间表的外键
            inverseJoinColumns = @JoinColumn(name = "user_id")  // 关联表（用户表）在中间表的外键
    )
    private Set<User> users = new HashSet<>();

    /**
     * 角色与权限的多对多关联（通过中间表 role_permission）
     */
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    /**
     * 预处理：创建时间自动填充
     */
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
}
