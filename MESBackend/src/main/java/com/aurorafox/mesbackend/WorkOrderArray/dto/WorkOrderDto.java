package com.aurorafox.mesbackend.WorkOrderArray.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 工单数据传输对象
 * 用于前端与后端之间的工单数据交互
 */
@Data
public class WorkOrderDto {

    /**
     * 工单编号
     */
    private String workOrderId;

    /**
     * 所属生产订单编号
     */
    private String orderId;

    /**
     * 产品编号
     */
    private String productId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 已排产数量
     */
    private Integer scheduledQuantity;

    /**
     * 完成数量
     */
    private Integer completedQuantity;

    /**
     * 工单状态
     */
    private String workOrderStatus;

    /**
     * 计划开工时间
     */
    private LocalDateTime plannedStartTime;

    /**
     * 计划完工时间
     */
    private LocalDateTime plannedEndTime;

    /**
     * 实际开工时间
     */
    private LocalDateTime actualStartTime;

    /**
     * 实际完工时间
     */
    private LocalDateTime actualEndTime;

    /**
     * 生产线编号
     */
    private String lineId;

    /**
     * 备注
     */
    private String remark;
}