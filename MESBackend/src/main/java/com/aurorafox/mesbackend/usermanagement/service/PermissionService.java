package com.aurorafox.mesbackend.usermanagement.service;

import com.aurorafox.mesbackend.usermanagement.dto.PermissionCreateRequest;
import com.aurorafox.mesbackend.usermanagement.dto.PermissionUpdateRequest;
import com.aurorafox.mesbackend.usermanagement.dto.PermissionDto;
import com.aurorafox.mesbackend.usermanagement.entity.Permission;
import com.aurorafox.mesbackend.usermanagement.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限服务类，封装权限相关的业务逻辑
 */
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    /** 创建权限 */
    public PermissionDto createPermission(PermissionCreateRequest request) {
        Permission permission = Permission.builder()
                .permissionId(request.getPermissionId()) // 前端传入
                .permissionName(request.getPermissionName())
                .permissionDesc(request.getPermissionDesc())
                .createTime(LocalDateTime.now())
                .build();

        Permission saved = permissionRepository.save(permission);
        return toDto(saved);
    }

    /** 根据ID读取权限信息 */
    public Optional<PermissionDto> getPermissionById(String permissionId) {
        return permissionRepository.findById(permissionId).map(this::toDto);
    }

    /** 读取所有权限信息 */
    public List<PermissionDto> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** 删除权限 */
    public boolean deletePermission(String permissionId) {
        if (permissionRepository.existsById(permissionId)) {
            permissionRepository.deleteById(permissionId);
            return true;
        }
        return false;
    }

    /** 更新权限信息 */
    public Optional<PermissionDto> updatePermission(String permissionId, PermissionUpdateRequest request) {
        return permissionRepository.findById(permissionId).map(existingPermission -> {
            existingPermission.setPermissionName(request.getPermissionName());
            existingPermission.setPermissionDesc(request.getPermissionDesc());
            Permission updated = permissionRepository.save(existingPermission);
            return toDto(updated);
        });
    }

    /* ---------- 私有工具方法 ---------- */
    private PermissionDto toDto(Permission entity) {
        return PermissionDto.builder()
                .permissionId(entity.getPermissionId())
                .permissionName(entity.getPermissionName())
                .permissionDesc(entity.getPermissionDesc())
                .createTime(entity.getCreateTime())
                .build();
    }
}
