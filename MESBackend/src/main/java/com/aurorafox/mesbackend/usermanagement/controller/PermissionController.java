package com.aurorafox.mesbackend.usermanagement.controller;

import com.aurorafox.mesbackend.usermanagement.dto.PermissionDto;
import com.aurorafox.mesbackend.usermanagement.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限 REST 接口
 * <p>
 * 提供权限的增删查改端点，路径统一前缀 {@code /api/permissions}。
 */
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 新增权限
     *
     * @param dto 权限信息
     * @return 201 Created + 保存后的权限
     */
    @PostMapping
    public ResponseEntity<PermissionDto> create(@Valid @RequestBody PermissionDto dto) {
        PermissionDto created = permissionService.createPermission(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 根据 ID 查询权限
     *
     * @param permissionId 权限编号
     * @return 200 OK + 权限信息
     */
    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionDto> getById(@PathVariable String permissionId) {
        PermissionDto dto = permissionService.getPermission(permissionId);
        return ResponseEntity.ok(dto);
    }

    /**
     * 查询全部权限
     *
     * @return 200 OK + 权限列表
     */
    @GetMapping
    public ResponseEntity<List<PermissionDto>> listAll() {
        List<PermissionDto> list = permissionService.listAllPermissions();
        return ResponseEntity.ok(list);
    }

    /**
     * 更新权限
     *
     * @param permissionId 权限编号（路径变量）
     * @param dto          更新的权限信息
     * @return 200 OK + 更新后的权限
     */
    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionDto> update(@PathVariable String permissionId,
                                                @Valid @RequestBody PermissionDto dto) {
        // 保证路径与体中的 ID 一致
        dto.setPermissionId(permissionId);
        PermissionDto updated = permissionService.updatePermission(dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除权限
     *
     * @param permissionId 权限编号
     * @return 204 No Content
     */
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> delete(@PathVariable String permissionId) {
        permissionService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }
}