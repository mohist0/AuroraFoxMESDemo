package com.aurorafox.mesbackend.security;

import com.aurorafox.mesbackend.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

/**
 * JwtAuthenticationFilter
 * ----------------------------
 * JWT 拦截过滤器：
 *  - 拦截每个请求，检查是否携带 JWT Token
 *  - 验证 Token 是否有效
 *  - 校验 Token 是否在黑名单中（退出登录后失效）
 *  - 如果有效，将用户信息、角色和权限放入 SecurityContext，供后续授权使用
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;          // JWT 工具类
    private final CustomUserDetailsService customUserDetailsService; // 用户信息加载服务
    private final AuthService authService;                    // 认证业务逻辑（含黑名单）

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   CustomUserDetailsService customUserDetailsService,
                                   AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.authService = authService;
    }

    /**
     * 核心过滤逻辑
     *
     * @param request HTTP 请求
     * @param response HTTP 响应
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);   // 无 Token 直接放过
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtTokenProvider.getUsernameFromToken(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (jwtTokenProvider.validateToken(token, userDetails)
                    && !authService.isTokenBlacklisted(token)) {

                String role = jwtTokenProvider.getRoleFromToken(token);
                List<String> permissions = jwtTokenProvider.getPermissionsFromToken(token);

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add((GrantedAuthority) () -> role);
                permissions.forEach(p -> authorities.add((GrantedAuthority) () -> p));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
