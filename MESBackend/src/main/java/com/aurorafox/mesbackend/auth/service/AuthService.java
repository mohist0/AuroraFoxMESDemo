package com.aurorafox.mesbackend.auth.service;

import com.aurorafox.mesbackend.auth.dto.LoginRequestDto;
import com.aurorafox.mesbackend.auth.dto.LoginResponseDto;
import com.aurorafox.mesbackend.security.JwtTokenProvider;
import com.aurorafox.mesbackend.usermanagement.entity.User;
import com.aurorafox.mesbackend.usermanagement.repository.UserRepository;
import com.aurorafox.mesbackend.common.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * AuthService
 * ----------------------------
 * 登录/退出业务逻辑：
 *  - 登录：校验用户名和密码，生成 JWT
 *  - 退出：将 Token 加入黑名单（可扩展）
 */
@Service
public class AuthService {

    private final UserRepository userRepository;   // 用户数据访问层
    private final JwtTokenProvider jwtTokenProvider; // JWT 工具类

    // 简单黑名单存储（可替换为 Redis）
    private final Set<String> tokenBlacklist = new HashSet<>();

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 用户登录逻辑
     */
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUserName(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 校验密码（使用盐值 + 哈希）
        if (!PasswordUtil.matches(request.getPassword(), user.getSaltValue(), user.getUserPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = jwtTokenProvider.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUserName())
                        .password(user.getUserPassword())
                        .authorities("ROLE_USER")
                        .build()
        );

        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        return response;
    }

    /**
     * 用户退出逻辑
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
     */
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}
