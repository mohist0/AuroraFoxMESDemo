package com.aurorafox.mesbackend.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aurorafox.mesbackend.usermanagement.entity.UserRole;
import java.util.List;

/**
 * 用户-角色中间表数据访问接口
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    /**
     * 根据角色ID查询关联的用户-角色记录
     * @param roleId 角色编号
     * @return 该角色关联的所有用户-角色记录
     */
    List<UserRole> findByRoleId(String roleId);

    /**
     * 根据用户ID和角色ID查询中间表记录
     * @param userId 用户编号
     * @param roleId 角色编号
     * @return 对应的中间表记录（存在则返回，不存在则返回null）
     */
    UserRole findByUserIdAndRoleId(String userId, String roleId);
}
