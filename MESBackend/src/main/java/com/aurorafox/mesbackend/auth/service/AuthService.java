package com.aurorafox.mesbackend.auth.service;

import com.aurorafox.mesbackend.auth.dto.LoginRequestDto;
import com.aurorafox.mesbackend.auth.dto.LoginResponseDto;
import com.aurorafox.mesbackend.security.JwtTokenProvider;
import com.aurorafox.mesbackend.usermanagement.entity.User;
import com.aurorafox.mesbackend.usermanagement.entity.UserRole;
import com.aurorafox.mesbackend.usermanagement.repository.UserRepository;
import com.aurorafox.mesbackend.usermanagement.repository.UserRoleRepository;
import com.aurorafox.mesbackend.common.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AuthService
 * ----------------------------
 * 登录/退出业务逻辑：
 *  - 登录：校验用户名和密码，校验角色绑定关系，生成 JWT
 *  - 退出：将 Token 加入黑名单（可扩展）
 */
@Service
public class AuthService {

    private final UserRepository userRepository;          // 用户数据访问层
    private final UserRoleRepository userRoleRepository;  // 用户角色关系访问层
    private final JwtTokenProvider jwtTokenProvider;      // JWT 工具类

    // 简单黑名单存储（可替换为 Redis）
    private final Set<String> tokenBlacklist = new HashSet<>();

    /**
     * 构造函数注入依赖
     */
    public AuthService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 用户登录逻辑
     *
     * @param request 登录请求 DTO（包含用户名、密码和角色ID）
     * @return 登录响应 DTO（包含 JWT 和角色信息）
     */
    public LoginResponseDto login(LoginRequestDto request) {
        // 1. 根据用户名查询用户
        User user = userRepository.findByUserName(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 校验密码（使用盐值 + 哈希）
        if (!PasswordUtil.matches(request.getPassword(), user.getSaltValue(), user.getUserPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 3. 校验角色绑定关系
        List<UserRole> roles = userRoleRepository.findByUserId(user.getUserId());
        boolean hasRole = roles.stream()
                .anyMatch(r -> r.getRoleId().equals(request.getRoleId()));
        if (!hasRole) {
            throw new RuntimeException("该用户未绑定所选角色");
        }

        // 4. 生成 JWT，写入用户名和角色信息
        String token = jwtTokenProvider.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUserName())
                        .password(user.getUserPassword())
                        .authorities(request.getRoleId()) // 使用选择的角色
                        .build(),
                request.getRoleId()
        );

        // 5. 封装响应 DTO
        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        response.setRoleId(request.getRoleId());
        response.setRoleName(request.getRoleId()); // 可替换为 RoleRepository 查询角色名称
        return response;
    }

    /**
     * 用户退出逻辑
     *
     * @param token JWT Token
     */
    public void logout(String token) {
        // 去掉 "Bearer " 前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 将 Token 加入黑名单
        tokenBlacklist.add(token);
    }

    /**
     * 检查 Token 是否在黑名单中
     *
     * @param token JWT Token
     * @return 是否在黑名单中
     */
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}
