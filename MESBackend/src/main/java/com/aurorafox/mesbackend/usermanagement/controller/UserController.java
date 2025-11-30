package com.aurorafox.mesbackend.usermanagement.controller;

import com.aurorafox.mesbackend.usermanagement.dto.UserCreateRequest;
import com.aurorafox.mesbackend.usermanagement.dto.UserDto;
import com.aurorafox.mesbackend.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器，提供用户相关的 REST API
 */
@Tag(name = "用户管理接口", description = "提供用户的增删查改及角色绑定功能")
@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAuthority('ROLE_R002')") // 整个控制器只能角色 系统管理员 访问
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "创建用户", description = "创建一个新用户并绑定角色", security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/addUser")
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateRequest request) {
        UserDto savedUser = userService.createUser(request);
        return ResponseEntity.ok(savedUser);
    }

    @Operation(summary = "根据ID获取用户信息", description = "通过用户编号查询用户详细信息", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/getUserById/{user_id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("user_id") String userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "获取所有用户信息", description = "查询系统中的所有用户", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "删除用户", description = "根据用户编号删除用户", security = { @SecurityRequirement(name = "bearerAuth") })
    @DeleteMapping("/deleteUser/{user_id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("user_id") String userId) {
        boolean deleted = userService.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 删除成功，无返回体
        } else {
            return ResponseEntity.notFound().build(); // 用户不存在
        }
    }

    @Operation(summary = "更新用户信息", description = "根据用户编号更新用户信息（可更新角色绑定）", security = { @SecurityRequirement(name = "bearerAuth") })
    @PutMapping("/updateUser/{user_id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("user_id") String userId,
                                              @RequestBody UserCreateRequest request) {
        return userService.updateUser(userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "查询用户绑定的角色信息", description = "根据用户编号查询其绑定的角色列表", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/{user_id}/roles")
    public ResponseEntity<List<String>> getUserRoles(@PathVariable("user_id") String userId) {
        List<String> roles = userService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }
}
