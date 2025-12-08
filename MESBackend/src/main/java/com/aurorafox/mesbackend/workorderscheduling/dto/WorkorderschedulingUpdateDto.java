package com.aurorafox.mesbackend.workorderscheduling.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

// 更新工单排程请求 DTO，对应 production_schedule 表
// 注意：不包含 scheduleId，主键由路径变量传入
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkorderschedulingUpdateDto {

    @NotBlank
    private String workOrderId; // 工单编号

    @NotBlank
    private String orderId; // 生产订单编号

    @NotBlank
    private String productId; // 产品编号

    @NotBlank
    private String lineId; // 生产线编号

    private String deviceId; // 设备编号，可为空

    @NotBlank
    private String materialId; // 物料编号

    private LocalDateTime plannedStartTime; // 计划开工时间

    private LocalDateTime plannedEndTime; // 计划完工时间

    private LocalDateTime actualStartTime; // 实际开工时间

    private LocalDateTime actualEndTime; // 实际完工时间

    private String remark; // 备注
}
