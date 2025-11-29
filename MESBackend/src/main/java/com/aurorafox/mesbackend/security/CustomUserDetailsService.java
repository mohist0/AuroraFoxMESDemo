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
 * 自定义用户信息加载服务
 * 作用：
 * - 根据用户名从数据库加载用户信息
 * - 转换为 Spring Security 的 UserDetails 对象
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

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
                .withUsername(user.getUserName())
                .password(user.getUserPassword()) // 已加密的密码
                .authorities((GrantedAuthority) Collections.singletonList("ROLE_USER")) // 默认角色，可扩展为从数据库加载
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
