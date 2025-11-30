package com.aurorafox.mesbackend.usermanagement.service;

import com.aurorafox.mesbackend.common.util.PasswordUtil;
import com.aurorafox.mesbackend.usermanagement.dto.UserCreateRequest;
import com.aurorafox.mesbackend.usermanagement.dto.UserDto;
import com.aurorafox.mesbackend.usermanagement.dto.UserMapper;
import com.aurorafox.mesbackend.usermanagement.entity.User;
import com.aurorafox.mesbackend.usermanagement.entity.UserRole;
import com.aurorafox.mesbackend.usermanagement.repository.UserRepository;
import com.aurorafox.mesbackend.usermanagement.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 用户服务类，封装用户相关的业务逻辑
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * 创建用户并绑定角色
     * @param request 用户创建请求体
     * @return 创建后的用户信息 DTO
     */
    public UserDto createUser(UserCreateRequest request) {
        // 生成盐值并加密密码
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(request.getPassword(), salt);

        // 构建用户实体
        User user = User.builder()
                .userId(request.getUserId())
                .userName(request.getUserName())
                .userPassword(hashedPassword)
                .saltValue(salt)
                .createTime(LocalDateTime.now())
                .build();

        // 保存用户
        User saved = userRepository.save(user);

        // 保存用户与角色的绑定关系
        if (request.getRoleIds() != null) {
            for (String roleId : request.getRoleIds()) {
                UserRole userRole = UserRole.builder()
                        .urId(UUID.randomUUID().toString()) // 主键用UUID生成
                        .userId(saved.getUserId())
                        .roleId(roleId)
                        .createTime(LocalDateTime.now())
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        return UserMapper.toDto(saved, userRoleRepository.findByUserId(saved.getUserId()));
    }

    /**
     * 根据ID读取用户信息
     * @param userId 用户编号
     * @return 用户信息 DTO
     */
    public Optional<UserDto> getUserById(String userId) {
        return userRepository.findById(userId).map(user -> {
            List<UserRole> roles = userRoleRepository.findByUserId(userId);
            return UserMapper.toDto(user, roles);
        });
    }

    /**
     * 读取所有用户信息
     * @return 用户信息列表
     */
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserMapper.toDto(user, userRoleRepository.findByUserId(user.getUserId())))
                .collect(Collectors.toList());
    }

    /**
     * 删除用户
     * @param userId 用户编号
     * @return 是否删除成功
     */
    public boolean deleteUserById(String userId) {
        if (userRepository.existsById(userId)) {
            // 删除用户角色绑定关系
            userRoleRepository.deleteAll(userRoleRepository.findByUserId(userId));
            // 删除用户
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    /**
     * 更新用户信息并更新角色绑定关系
     * @param userId 用户编号（来自路径参数）
     * @param request 用户更新请求体
     * @return 更新后的用户信息 DTO
     */
    public Optional<UserDto> updateUser(String userId, UserCreateRequest request) {
        return userRepository.findById(userId).map(existingUser -> {
            // 不允许修改 userId，只能更新其他字段
            existingUser.setUserName(request.getUserName());

            // 如果传了新密码，则重新生成盐值并加密
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                String salt = PasswordUtil.generateSalt();
                String hashedPassword = PasswordUtil.hashPassword(request.getPassword(), salt);
                existingUser.setSaltValue(salt);
                existingUser.setUserPassword(hashedPassword);
            }

            User updated = userRepository.save(existingUser);

            // 更新用户角色绑定关系（先删除旧的，再插入新的）
            if (request.getRoleIds() != null) {
                userRoleRepository.deleteAll(userRoleRepository.findByUserId(userId));
                for (String roleId : request.getRoleIds()) {
                    UserRole userRole = UserRole.builder()
                            .urId(UUID.randomUUID().toString())
                            .userId(updated.getUserId()) // 使用路径参数中的 userId
                            .roleId(roleId)
                            .createTime(LocalDateTime.now())
                            .build();
                    userRoleRepository.save(userRole);
                }
            }

            return UserMapper.toDto(updated, userRoleRepository.findByUserId(updated.getUserId()));
        });
    }

    /**
     * 查询用户绑定的角色ID列表
     * @param userId 用户编号
     * @return 角色ID列表
     */
    public List<String> getUserRoles(String userId) {
        return userRoleRepository.findByUserId(userId).stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }
}
