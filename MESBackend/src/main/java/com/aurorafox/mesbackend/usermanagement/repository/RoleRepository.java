package com.aurorafox.mesbackend.usermanagement.repository;
import com.aurorafox.mesbackend.usermanagement.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 角色数据访问接口，用于与数据库进行交互。
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    // 可以在此添加更多的查询方法
    Role findByName(String name);
}
