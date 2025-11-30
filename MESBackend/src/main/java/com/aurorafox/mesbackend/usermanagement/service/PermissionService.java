package com.aurorafox.mesbackend.usermanagement.service;

import com.aurorafox.mesbackend.usermanagement.dto.PermissionDto;
import com.aurorafox.mesbackend.usermanagement.entity.Permission;
import com.aurorafox.mesbackend.usermanagement.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限业务逻辑服务
 */
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    /**
     * 新增权限
     *
     * @param dto 权限 DTO
     * @return 保存后的权限 DTO
     */
    @Transactional
    public PermissionDto createPermission(PermissionDto dto) {
        Permission entity = new Permission();
        BeanUtils.copyProperties(dto, entity);
        entity.setCreateTime(LocalDateTime.now());
        Permission saved = permissionRepository.save(entity);
        return toDto(saved);
    }

    /**
     * 根据 ID 查询权限
     *
     * @param permissionId 权限编号
     * @return 权限 DTO
     * @throws IllegalArgumentException 权限不存在
     */
    @Transactional(readOnly = true)
    public PermissionDto getPermission(String permissionId) {
        Permission entity = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("权限不存在: " + permissionId));
        return toDto(entity);
    }

    /**
     * 查询全部权限
     *
     * @return 权限 DTO 列表
     */
    @Transactional(readOnly = true)
    public List<PermissionDto> listAllPermissions() {
        return permissionRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 更新权限
     *
     * @param dto 权限 DTO（必须包含 permissionId）
     * @return 更新后的权限 DTO
     * @throws IllegalArgumentException 权限不存在
     */
    @Transactional
    public PermissionDto updatePermission(PermissionDto dto) {
        Permission entity = permissionRepository.findById(dto.getPermissionId())
                .orElseThrow(() -> new IllegalArgumentException("权限不存在: " + dto.getPermissionId()));
        // 不更新 createTime
        BeanUtils.copyProperties(dto, entity, "createTime");
        Permission updated = permissionRepository.save(entity);
        return toDto(updated);
    }

    /**
     * 删除权限
     *
     * @param permissionId 权限编号
     * @throws IllegalArgumentException 权限不存在
     */
    @Transactional
    public void deletePermission(String permissionId) {
        if (!permissionRepository.existsById(permissionId)) {
            throw new IllegalArgumentException("权限不存在: " + permissionId);
        }
        permissionRepository.deleteById(permissionId);
    }

    /* ---------- 私有工具方法 ---------- */

    private PermissionDto toDto(Permission entity) {
        PermissionDto dto = new PermissionDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}