package com.aurorafox.mesbackend.usermanagement.repository;

import com.aurorafox.mesbackend.usermanagement.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 权限数据访问接口
 * <p>
 * 继承 {@link JpaRepository}，提供基础的 CRUD 与分页查询能力。
 */
public interface PermissionRepository extends JpaRepository<Permission, String> {
    // 可按需扩展自定义查询方法，例如：
    // List<Permission> findByPermissionType(String type);
}