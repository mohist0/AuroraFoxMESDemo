package com.aurorafox.mesbackend.usermanagement.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.aurorafox.mesbackend.usermanagement.dto.RoleDto;
import com.aurorafox.mesbackend.usermanagement.service.RoleService;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 角色控制器
 * 提供角色管理的REST API接口
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色相关操作接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 创建角色
     * @param roleDto 角色DTO（JSON格式）
     * @return 201 Created + 创建成功的角色信息
     */
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        try {
            RoleDto createdRole = roleService.createRole(roleDto);
            return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // 参数验证失败（ID重复、名称重复）
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 根据角色ID查询角色详情
     * @param roleId 角色编号（路径参数）
     * @return 200 OK + 角色详情DTO
     */
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable String roleId) {
        try {
            RoleDto roleDto = roleService.getRoleById(roleId);
            return ResponseEntity.ok(roleDto);
        } catch (NoSuchElementException e) {
            // 角色不存在
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 查询所有角色
     * @return 200 OK + 角色列表
     */
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roleDtoList = roleService.getAllRoles();
        return ResponseEntity.ok(roleDtoList);
    }

    /**
     * 根据角色名称模糊查询
     * @param roleName 角色名称关键字（请求参数）
     * @return 200 OK + 符合条件的角色列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<RoleDto>> searchRolesByRoleName(@RequestParam String roleName) {
        List<RoleDto> roleDtoList = roleService.getRolesByRoleName(roleName);
        return ResponseEntity.ok(roleDtoList);
    }

    /**
     * 更新角色信息
     * @param roleId 角色编号（路径参数）
     * @param roleDto 角色更新DTO（JSON格式）
     * @return 200 OK + 更新后的角色信息
     */
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDto> updateRole(
            @PathVariable String roleId,
            @RequestBody RoleDto roleDto) {
        try {
            RoleDto updatedRole = roleService.updateRole(roleId, roleDto);
            return ResponseEntity.ok(updatedRole);
        } catch (NoSuchElementException e) {
            // 角色不存在
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            // 名称重复等参数错误
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 删除角色
     * @param roleId 角色编号（路径参数）
     * @return 204 No Content（删除成功）
     */
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable String roleId) {
        try {
            roleService.deleteRole(roleId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            // 角色不存在
            return ResponseEntity.notFound().build();
        }
    }
}