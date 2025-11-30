package com.aurorafox.mesbackend.auth.controller;

import com.aurorafox.mesbackend.auth.dto.LoginRequestDto;
import com.aurorafox.mesbackend.auth.dto.LoginResponseDto;
import com.aurorafox.mesbackend.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 * ----------------------------
 * 提供认证相关接口：
 *  - /api/auth/login 登录接口
 *  - /api/auth/logout 退出登录接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService; // 登录业务逻辑层

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 登录接口
     *
     * @param request 登录请求 DTO
     * @return 登录响应 DTO（包含 JWT）
     */
    @Operation(
            summary = "用户登录接口",
            description = "用户提交用户名和密码，验证成功后返回 JWT Token。后续请求需在 Header 中携带 Authorization: Bearer <token>。",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    /**
     * 退出登录接口
     *
     * @param token JWT Token（从请求头传入）
     * @return 提示信息
     */
    @Operation(
            summary = "用户退出登录接口",
            description = "用户退出登录时调用该接口，后端会将当前 JWT Token 加入黑名单，后续请求将失效。",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return "退出登录成功";
    }
}
