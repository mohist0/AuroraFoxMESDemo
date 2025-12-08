package com.aurorafox.mesbackend.workorderscheduling.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * 更新工单排程请求 DTO
 * <p>
 * 用于更新操作，必须包含 scheduleId。
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkorderschedulingUpdateDto {

    @NotBlank
    private String scheduleId;

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