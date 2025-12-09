package com.aurorafox.mesbackend.workorderscheduling.dto;

import lombok.*;
import java.time.LocalDateTime;

// 工单排程响应 DTO，对应 production_schedule 表
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkorderschedulingDto {

    private String scheduleId; // 排程编号

    private String workOrderId; // 工单编号

    private String orderId; // 生产订单编号

    private String productId; // 产品编号

    private String lineId; // 生产线编号

    private String deviceId; // 设备编号

    private String materialId; // 物料编号

    private LocalDateTime plannedStartTime; // 计划开工时间

    private LocalDateTime plannedEndTime; // 计划完工时间

    private LocalDateTime actualStartTime; // 实际开工时间

    private LocalDateTime actualEndTime; // 实际完工时间

    private String remark; // 备注

    private LocalDateTime createTime; // 创建时间

    private LocalDateTime updateTime; // 更新时间
}
