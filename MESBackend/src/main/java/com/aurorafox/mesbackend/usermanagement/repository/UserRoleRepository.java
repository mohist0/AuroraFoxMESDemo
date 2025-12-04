package com.aurorafox.mesbackend.usermanagement.repository;

import com.aurorafox.mesbackend.usermanagement.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关系 Repository
 * 提供对 user_role 表的数据库访问
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    // 根据用户ID查询角色绑定关系
    List<UserRole> findByUserId(String userId);
}
