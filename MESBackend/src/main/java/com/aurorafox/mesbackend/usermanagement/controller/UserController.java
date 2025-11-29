package com.aurorafox.mesbackend.usermanagement.controller;

import com.aurorafox.mesbackend.usermanagement.dto.UserCreateRequest;
import com.aurorafox.mesbackend.usermanagement.dto.UserDto;
import com.aurorafox.mesbackend.usermanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 创建用户
    @PostMapping("/addUser")
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateRequest request) {
        UserDto savedUser = userService.createUser(request);
        return ResponseEntity.ok(savedUser);
    }

    // 根据ID获取用户信息
    @GetMapping("/getUserById/{user_id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("user_id") String userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 获取所有用户信息
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 删除用户
    @DeleteMapping("/deleteUser/{user_id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("user_id") String userId) {
        boolean deleted = userService.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 删除成功，无返回体
        } else {
            return ResponseEntity.notFound().build(); // 用户不存在
        }
    }

    // 更新用户信息
    @PutMapping("/updateUser/{user_id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("user_id") String userId,
                                              @RequestBody UserCreateRequest request) {
        return userService.updateUser(userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
