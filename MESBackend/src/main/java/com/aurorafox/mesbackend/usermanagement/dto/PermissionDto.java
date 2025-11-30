package com.aurorafox.mesbackend.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限数据传输对象（DTO）
 * <p>
 * 用于前后端交互，屏蔽实体细节。
 */
@Data
public class PermissionDto {

    /** 权限编号 */
    private String permissionId;

    /** 权限名称 */
    @NotBlank(message = "权限名称不能为空")
    private String permissionName;

    /** 权限类型 */
    private String permissionType;

    /** 权限说明 */
    private String permissionDesc;

    /** 创建时间，仅输出 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}