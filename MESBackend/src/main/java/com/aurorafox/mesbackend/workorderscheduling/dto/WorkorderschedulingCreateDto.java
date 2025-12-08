package com.aurorafox.mesbackend.workorderscheduling.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * 新建工单排程请求 DTO
 * <p>
 * 用于创建操作，不包含 scheduleId、createTime、updateTime（由服务端生成/维护）。
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkorderschedulingCreateDto {

    @NotBlank
    private String workOrderId;

    @NotBlank
    private String orderId;

    @NotBlank
    private String productId;

    @NotBlank
    private String lineId;

    private String deviceId;

    @NotBlank
    private String materialId;

    private LocalDateTime plannedStartTime;

    private LocalDateTime plannedEndTime;

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    private String remark;
}