package com.aurorafox.mesbackend.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {

    // 生成随机盐值
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }

    // 使用 SHA-256 加密（密码 + 盐）
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = password + salt;
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 算法不可用", e);
        }
    }

    // 校验密码
    public static boolean verifyPassword(String rawPassword, String salt, String hashedPassword) {
        String hash = hashPassword(rawPassword, salt);
        return hash.equals(hashedPassword);
    }

    // 工具方法：字节数组转十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 新增方法：简化调用
    public static boolean matches(String rawPassword, String salt, String hashedPassword) {
        return verifyPassword(rawPassword, salt, hashedPassword);
    }
}
