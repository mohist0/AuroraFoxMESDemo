package com.aurorafox.mesbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * JwtTokenProvider
 * ----------------------------
 * JWT 工具类：
 *  - 生成 JWT Token（包含角色和权限信息）
 *  - 解析 Token 获取用户名、角色和权限
 *  - 验证 Token 是否有效
 */
@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long validityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    /**
     * 生成 JWT Token
     * @param userDetails 用户信息
     * @param roleId 登录时选择的角色ID
     * @param permissions 当前角色对应的权限列表
     * @return JWT 字符串
     */
    public String generateToken(UserDetails userDetails, String roleId, List<String> permissions) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", "ROLE_" + roleId) // 存储带前缀的角色
                .claim("permissions", permissions)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从 Token 中解析用户名
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 从 Token 中解析角色
     */
    public String getRoleFromToken(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().get("role");
    }

    /**
     * 从 Token 中解析权限列表
     */
    @SuppressWarnings("unchecked")
    public List<String> getPermissionsFromToken(String token) {
        return (List<String>) Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().get("permissions");
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 判断 Token 是否过期
     */
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }
}
