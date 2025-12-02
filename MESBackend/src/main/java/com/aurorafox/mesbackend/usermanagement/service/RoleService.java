package com.aurorafox.mesbackend.usermanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aurorafox.mesbackend.usermanagement.dto.RoleDto;
import com.aurorafox.mesbackend.usermanagement.entity.Role;
import com.aurorafox.mesbackend.usermanagement.entity.RolePermission;
import com.aurorafox.mesbackend.usermanagement.entity.UserRole;
import com.aurorafox.mesbackend.usermanagement.repository.RolePermissionRepository;
import com.aurorafox.mesbackend.usermanagement.repository.RoleRepository;
import com.aurorafox.mesbackend.usermanagement.repository.UserRoleRepository;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色业务逻辑服务类
 * 封装角色的增删查改及关联操作（角色-用户、角色-权限）
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * 创建角色
     * @param roleDto 角色DTO（包含角色名称、说明、权限ID集合）
     * @return 创建成功的角色DTO
     * @throws IllegalArgumentException 角色ID已存在或角色名称重复时抛出
     */
    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        // 1. 验证角色ID是否已存在
        if (roleRepository.existsById(roleDto.getRoleId())) {
            throw new IllegalArgumentException("角色ID：" + roleDto.getRoleId() + " 已存在");
        }

        // 2. 验证角色名称是否重复
        roleRepository.findByRoleName(roleDto.getRoleName())
                .ifPresent(role -> {
                    throw new IllegalArgumentException("角色名称：" + roleDto.getRoleName() + " 已存在");
                });

        // 3. DTO 转 Entity
        Role role = new Role();
        BeanUtils.copyProperties(roleDto, role);

        // 4. 保存角色基本信息
        Role savedRole = roleRepository.save(role);

        // 5. 关联权限（如果传入了权限ID集合）
        if (Objects.nonNull(roleDto.getPermissionIds()) && !roleDto.getPermissionIds().isEmpty()) {
            Set<RolePermission> rolePermissions = roleDto.getPermissionIds().stream()
                    .map(permissionId -> {
                        RolePermission rp = new RolePermission();
                        rp.setRoleId(savedRole.getRoleId());
                        rp.setPermissionId(permissionId);
                        return rp;
                    })
                    .collect(Collectors.toSet());
            rolePermissionRepository.saveAll(rolePermissions);
        }

        // 6. Entity 转 DTO 并返回
        RoleDto resultDto = new RoleDto();
        BeanUtils.copyProperties(savedRole, resultDto);
        resultDto.setPermissionIds(roleDto.getPermissionIds());
        return resultDto;
    }

    /**
     * 根据角色ID查询角色详情（包含关联的用户ID和权限ID）
     * @param roleId 角色编号
     * @return 角色详情DTO
     * @throws NoSuchElementException 角色不存在时抛出
     */
    public RoleDto getRoleById(String roleId) {
        // 1. 查询角色基本信息
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("角色ID：" + roleId + " 不存在"));

        // 2. 查询关联的权限ID集合
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        Set<String> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toSet());

        // 3. 查询关联的用户ID集合
        List<UserRole> userRoles = userRoleRepository.findByRoleId(roleId);
        Set<String> userIds = userRoles.stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toSet());

        // 4. 封装DTO并返回
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(role, roleDto);
        roleDto.setPermissionIds(permissionIds);
        roleDto.setUserIds(userIds);
        return roleDto;
    }

    /**
     * 查询所有角色（仅返回基本信息，不包含关联数据）
     * @return 角色DTO列表
     */
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> {
                    RoleDto dto = new RoleDto();
                    BeanUtils.copyProperties(role, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据角色名称模糊查询角色
     * @param roleName 角色名称关键字
     * @return 符合条件的角色DTO列表
     */
    public List<RoleDto> getRolesByRoleName(String roleName) {
        return ((List<Role>) roleRepository.findByRoleNameContaining(roleName)).stream()
                .map(role -> {
                    RoleDto dto = new RoleDto();
                    BeanUtils.copyProperties(role, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 更新角色信息（支持更新名称、说明、权限关联）
     * @param roleId 角色编号（路径参数，确保更新的是指定角色）
     * @param roleDto 角色更新DTO
     * @return 更新后的角色DTO
     * @throws NoSuchElementException 角色不存在时抛出
     * @throws IllegalArgumentException 角色名称重复时抛出
     */
    @Transactional
    public RoleDto updateRole(String roleId, RoleDto roleDto) {
        // 1. 验证角色是否存在
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("角色ID：" + roleId + " 不存在"));

        // 2. 验证角色名称是否重复（如果修改了名称）
        if (!Objects.equals(existingRole.getRoleName(), roleDto.getRoleName())) {
            roleRepository.findByRoleName(roleDto.getRoleName())
                    .ifPresent(role -> {
                        throw new IllegalArgumentException("角色名称：" + roleDto.getRoleName() + " 已存在");
                    });
        }

        // 3. 更新角色基本信息（忽略roleId和createTime，避免被修改）
        existingRole.setRoleName(roleDto.getRoleName());
        existingRole.setRoleDesc(roleDto.getRoleDesc());
        Role updatedRole = roleRepository.save(existingRole);

        // 4. 更新权限关联（先删除原有关联，再添加新关联）
        if (Objects.nonNull(roleDto.getPermissionIds())) {
            // 删除原有权限关联
            rolePermissionRepository.deleteAll(rolePermissionRepository.findByRoleId(roleId));

            // 添加新的权限关联（如果权限ID集合非空）
            if (!roleDto.getPermissionIds().isEmpty()) {
                Set<RolePermission> newRolePermissions = roleDto.getPermissionIds().stream()
                        .map(permissionId -> {
                            RolePermission rp = new RolePermission();
                            rp.setRoleId(roleId);
                            rp.setPermissionId(permissionId);
                            return rp;
                        })
                        .collect(Collectors.toSet());
                rolePermissionRepository.saveAll(newRolePermissions);
            }
        }

        // 5. 封装DTO并返回
        RoleDto resultDto = new RoleDto();
        BeanUtils.copyProperties(updatedRole, resultDto);
        resultDto.setPermissionIds(roleDto.getPermissionIds());
        return resultDto;
    }

    /**
     * 根据角色ID删除角色（级联删除关联的用户-角色、角色-权限记录）
     * @param roleId 角色编号
     * @throws NoSuchElementException 角色不存在时抛出
     */
    @Transactional
    public void deleteRole(String roleId) {
        // 1. 验证角色是否存在
        if (!roleRepository.existsById(roleId)) {
            throw new NoSuchElementException("角色ID：" + roleId + " 不存在");
        }

        // 2. 级联删除中间表记录（先删关联，再删主表）
        rolePermissionRepository.deleteAll(rolePermissionRepository.findByRoleId(roleId));
        userRoleRepository.deleteAll(userRoleRepository.findByRoleId(roleId));

        // 3. 删除角色主表记录
        roleRepository.deleteById(roleId);
    }
}