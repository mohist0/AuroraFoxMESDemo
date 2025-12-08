package com.aurorafox.mesbackend.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aurorafox.mesbackend.usermanagement.entity.Role;
import java.util.Optional;

/**
 * 角色数据访问接口（JPA Repository）
 * 继承 JpaRepository 提供基础的 CRUD 操作，无需手动实现
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    /**
     * 根据角色名称查询角色（用于判断角色名称是否重复）
     * @param roleName 角色名称
     * @return 角色实体（Optional 包装，避免空指针）
     */
    Optional<Role> findByRoleName(String roleName);

    /**
     * 根据角色名称模糊查询角色列表
     * @param roleName 角色名称关键字
     * @return 符合条件的角色集合
     */
    Iterable<Role> findByRoleNameContaining(String roleName);
}