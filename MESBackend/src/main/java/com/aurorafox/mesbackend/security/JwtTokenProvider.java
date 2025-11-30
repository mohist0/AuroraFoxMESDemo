package com.aurorafox.mesbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtTokenProvider
 * ----------------------------
 * JWT 工具类：
 *  - 生成 JWT Token（包含角色信息）
 *  - 解析 Token 获取用户名和角色
 *  - 验证 Token 是否有效
 */
@Component
public class JwtTokenProvider {

    private final Key secretKey;                // 用于签名和校验的密钥
    private final long validityInMilliseconds;  // Token 有效期（毫秒）

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
     * @return JWT 字符串
     */
    public String generateToken(UserDetails userDetails, String roleId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // 设置用户名
                .claim("role", roleId)                 // 写入角色信息
                .setIssuedAt(now)                      // 签发时间
                .setExpiration(expiryDate)             // 过期时间
                .signWith(secretKey, SignatureAlgorithm.HS256) // 使用 HS256 算法签名
                .compact();
    }

    /**
     * 从 Token 中解析用户名
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 从 Token 中解析角色
     */
    public String getRoleFromToken(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
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
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
