package com.aurorafox.mesbackend.workorderscheduling.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 工单排程响应 DTO
 * <p>
 * 用于向客户端返回完整的排程信息，包括主键与时间字段。
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkorderschedulingDto {

    private String scheduleId;

    private String workOrderId;

    private String orderId;

    private String productId;

    private String lineId;

    private String deviceId;

    private String materialId;

    private LocalDateTime plannedStartTime;

    private LocalDateTime plannedEndTime;

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}