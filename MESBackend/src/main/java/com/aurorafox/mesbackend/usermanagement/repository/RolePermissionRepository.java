package com.aurorafox.mesbackend.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aurorafox.mesbackend.usermanagement.entity.RolePermission;
import java.util.List;

/**
 * 角色-权限中间表数据访问接口
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, String> {

    /**
     * 根据角色ID查询关联的角色-权限记录
     * @param roleId 角色编号
     * @return 该角色关联的所有权限记录
     */
    List<RolePermission> findByRoleId(String roleId);

    /**
     * 根据角色ID和权限ID查询中间表记录
     * @param roleId 角色编号
     * @param permissionId 权限编号
     * @return 对应的中间表记录
     */
    RolePermission findByRoleIdAndPermissionId(String roleId, String permissionId);

    /**
     * 根据权限ID删除所有关联的角色-权限记录
     * @param permissionId 权限编号
     */
    void deleteByPermissionId(String permissionId);
}