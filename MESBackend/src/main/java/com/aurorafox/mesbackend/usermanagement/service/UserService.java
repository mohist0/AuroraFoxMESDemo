package com.aurorafox.mesbackend.usermanagement.service;

import com.aurorafox.mesbackend.common.util.PasswordUtil;
import com.aurorafox.mesbackend.usermanagement.dto.UserCreateRequest;
import com.aurorafox.mesbackend.usermanagement.dto.UserDto;
import com.aurorafox.mesbackend.usermanagement.dto.UserMapper;
import com.aurorafox.mesbackend.usermanagement.entity.User;
import com.aurorafox.mesbackend.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户服务类，封装用户相关的业务逻辑
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 创建用户（自动生成盐值并加密密码）
    public UserDto createUser(UserCreateRequest request) {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(request.getPassword(), salt);

        User user = User.builder()
                .userId(request.getUserId())
                .userName(request.getUserName())
                .userPassword(hashedPassword)
                .saltValue(salt)
                .createTime(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

    // 根据ID读取用户信息
    public Optional<UserDto> getUserById(String userId) {
        return userRepository.findById(userId).map(UserMapper::toDto);
    }

    // 读取所有用户信息
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    // 删除用户
    public boolean deleteUserById(String userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    // 更新用户信息
    public Optional<UserDto> updateUser(String userId, UserCreateRequest request) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setUserName(request.getUserName());

            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                String salt = PasswordUtil.generateSalt();
                String hashedPassword = PasswordUtil.hashPassword(request.getPassword(), salt);
                existingUser.setSaltValue(salt);
                existingUser.setUserPassword(hashedPassword);
            }

            User updated = userRepository.save(existingUser);
            return UserMapper.toDto(updated);
        });
    }
}
