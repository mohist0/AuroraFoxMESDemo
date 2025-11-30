package com.aurorafox.mesbackend.security;

import com.aurorafox.mesbackend.usermanagement.entity.User;
import com.aurorafox.mesbackend.usermanagement.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * CustomUserDetailsService
 * ----------------------------
 * 自定义用户信息加载服务：
 *  - 根据用户名从数据库加载用户信息
 *  - 转换为 Spring Security 的 UserDetails 对象
 *
 * 使用场景：
 *  - 被 JwtAuthenticationFilter 调用，用于校验 Token 时加载用户信息
 *  - 被 AuthenticationManager 调用，用于登录认证
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // 用户数据访问层

    /**
     * 构造函数注入依赖
     *
     * @param userRepository 用户数据仓库
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 根据用户名加载用户信息
     *
     * @param username 用户名
     * @return UserDetails 对象（包含用户名、密码、权限）
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查找用户
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 将 User 转换为 Spring Security 的 UserDetails
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())          // 用户名
                .password(user.getUserPassword())          // 已加密的密码
                .authorities((GrantedAuthority) () -> "ROLE_USER") // 默认角色，可扩展为从数据库加载
                .accountExpired(false)                     // 账号未过期
                .accountLocked(false)                      // 账号未锁定
                .credentialsExpired(false)                 // 凭证未过期
                .disabled(false)                           // 账号未禁用
                .build();
    }
}
