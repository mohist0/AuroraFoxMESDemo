package com.aurorafox.mesbackend.auth.service;

import com.aurorafox.mesbackend.auth.dto.LoginRequestDto;
import com.aurorafox.mesbackend.auth.dto.LoginResponseDto;
import com.aurorafox.mesbackend.security.JwtTokenProvider;
import com.aurorafox.mesbackend.usermanagement.entity.User;
import com.aurorafox.mesbackend.usermanagement.entity.UserRole;
import com.aurorafox.mesbackend.usermanagement.entity.RolePermission;
import com.aurorafox.mesbackend.usermanagement.repository.UserRepository;
import com.aurorafox.mesbackend.usermanagement.repository.UserRoleRepository;
import com.aurorafox.mesbackend.usermanagement.repository.RolePermissionRepository;
import com.aurorafox.mesbackend.common.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AuthService
 * ----------------------------
 * 登录/退出业务逻辑：
 *  - 登录：校验用户名和密码，校验角色绑定关系，生成 JWT 并返回权限
 *  - 退出：将 Token 加入黑名单（可扩展）
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 简单黑名单存储（可替换为 Redis）
    private final Set<String> tokenBlacklist = new HashSet<>();

    public AuthService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       RolePermissionRepository rolePermissionRepository,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 用户登录逻辑
     */
    public LoginResponseDto login(LoginRequestDto request) {
        // 1. 根据用户名查询用户
        User user = userRepository.findByUserName(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 校验密码
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

        // 4. 查询角色对应的权限列表
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(request.getRoleId());
        List<String> permissions = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());

        // 5. 生成 JWT，写入用户名、角色和权限信息
        String roleAuthority = "ROLE_" + request.getRoleId(); // 统一加前缀

        String token = jwtTokenProvider.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUserName())
                        .password(user.getUserPassword())
                        .authorities(roleAuthority) // 只注入当前选择的角色
                        .build(),
                request.getRoleId(),
                permissions
        );

        // 6. 封装响应 DTO
        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        response.setRoleId(request.getRoleId());
        response.setRoleName(request.getRoleId()); // 可替换为 RoleRepository 查询角色名称
        response.setPermissions(permissions);
        return response;
    }

    /**
     * 用户退出逻辑
     */
    public void logout(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        tokenBlacklist.add(token);
    }

    /**
     * 检查 Token 是否在黑名单中
     */
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}
