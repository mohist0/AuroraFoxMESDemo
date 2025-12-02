package com.aurorafox.mesbackend.usermanagement.controller;

import com.aurorafox.mesbackend.usermanagement.dto.PermissionCreateRequest;
import com.aurorafox.mesbackend.usermanagement.dto.PermissionUpdateRequest;
import com.aurorafox.mesbackend.usermanagement.dto.PermissionDto;
import com.aurorafox.mesbackend.usermanagement.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器，提供权限相关的 REST API
 */
@Tag(name = "权限管理接口", description = "提供权限的增删查改功能")
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Operation(summary = "新增权限", description = "创建一个新的权限", security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/addPermission")
    public ResponseEntity<PermissionDto> createPermission(@Valid @RequestBody PermissionCreateRequest request) {
        PermissionDto created = permissionService.createPermission(request);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "根据ID获取权限信息", description = "通过权限编号查询权限详细信息", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/getPermissionById/{permission_id}")
    public ResponseEntity<PermissionDto> getPermissionById(@PathVariable("permission_id") String permissionId) {
        return permissionService.getPermissionById(permissionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "获取所有权限信息", description = "查询系统中的所有权限", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/getAllPermissions")
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @Operation(summary = "更新权限信息", description = "根据权限编号更新权限信息", security = { @SecurityRequirement(name = "bearerAuth") })
    @PutMapping("/updatePermission/{permission_id}")
    public ResponseEntity<PermissionDto> updatePermission(@PathVariable("permission_id") String permissionId,
                                                          @Valid @RequestBody PermissionUpdateRequest request) {
        return permissionService.updatePermission(permissionId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "删除权限", description = "根据权限编号删除权限", security = { @SecurityRequirement(name = "bearerAuth") })
    @DeleteMapping("/deletePermission/{permission_id}")
    public ResponseEntity<Void> deletePermission(@PathVariable("permission_id") String permissionId) {
        boolean deleted = permissionService.deletePermission(permissionId);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 删除成功
        } else {
            return ResponseEntity.notFound().build(); // 权限不存在
        }
    }
}
