package com.aurorafox.mesbackend.usermanagement.repository;

import com.aurorafox.mesbackend.usermanagement.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RolePermissionRepository
 * ----------------------------
 * 角色权限关系表仓储接口
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, String> {

    /**
     * 根据角色ID查询该角色绑定的权限列表
     * @param roleId 角色ID
     * @return 权限关系列表
     */
    List<RolePermission> findByRoleId(String roleId);
}
