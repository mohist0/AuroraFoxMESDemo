package com.aurorafox.mesbackend.usermanagement.controller;

import com.aurorafox.mesbackend.usermanagement.dto.RoleDto;
import com.aurorafox.mesbackend.usermanagement.entity.Role;
import com.aurorafox.mesbackend.usermanagement.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 角色控制器，提供角色管理 REST API。
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 创建角色
     */
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(roleService.createRole(roleDto));
    }

    /**
     * 查询所有角色
     */
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    /**
     * 根据 ID 查询角色
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Role>> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id,
                                           @RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDto));
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}