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

    /** 角色编号，主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "role")
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<RolePermission> rolePermissions = new HashSet<>();

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Set<RolePermission> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(Set<RolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }
}
