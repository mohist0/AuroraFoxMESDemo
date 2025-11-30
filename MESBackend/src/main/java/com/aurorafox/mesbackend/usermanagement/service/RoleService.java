package com.aurorafox.mesbackend.usermanagement.service;

import com.aurorafox.mesbackend.usermanagement.dto.RoleDto;
import com.aurorafox.mesbackend.usermanagement.entity.Role;
import com.aurorafox.mesbackend.usermanagement.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 角色服务层，封装角色的增删查改操作的业务逻辑。
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 创建新角色
     *
     * @param roleDto 角色数据传输对象
     * @return 创建的角色
     */
    public Role createRole(RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        return roleRepository.save(role);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    /**
     * 获取所有角色
     *
     * @return 角色列表
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * 根据ID查找角色
     *
     * @param id 角色ID
     * @return 角色实体
     */
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * 更新角色信息
     *
     * @param id      角色ID
     * @param roleDto 更新后的角色数据传输对象
     * @return 更新后的角色实体
     */
    public Role updateRole(Long id, RoleDto roleDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        return roleRepository.save(role);
    }
}
