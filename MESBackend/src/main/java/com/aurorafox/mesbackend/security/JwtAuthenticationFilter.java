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
import java.util.List;

/**
 * JwtAuthenticationFilter
 * ----------------------------
 * JWT 拦截过滤器：
 *  - 拦截每个请求，检查是否携带 JWT Token
 *  - 验证 Token 是否有效
 *  - 校验 Token 是否在黑名单中（退出登录后失效）
 *  - 如果有效，将用户信息和角色信息放入 SecurityContext，供后续授权使用
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
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 从请求头获取 Token
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 去掉 "Bearer " 前缀
            username = jwtTokenProvider.getUsernameFromToken(token);
        }

        // 验证 Token 并设置认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // 校验 Token 有效性 + 黑名单检查
            if (jwtTokenProvider.validateToken(token, userDetails)
                    && !authService.isTokenBlacklisted(token)) {

                // 从 Token 中解析角色信息
                String role = jwtTokenProvider.getRoleFromToken(token);

                // 构造认证对象，包含角色信息
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, List.of(() -> role));

                // 设置请求详情（IP、Session 等）
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将认证信息放入 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 继续执行过滤链
        filterChain.doFilter(request, response);
    }
}
