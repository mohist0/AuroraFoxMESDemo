package com.aurorafox.mesbackend.WorkOrderArray.dto;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 工单查询条件DTO
 * 用于接收前端的工单查询参数
 */
@Data
public class WorkOrderQueryDto
{

    /**
     * 工单编号（模糊查询）
     */
    private String workOrderId;

    /**
     * 生产订单编号
     */
    private String orderId;

    /**
     * 产品编号
     */
    private String productId;

    /**
     * 生产线编号
     */
    private String lineId;

    /**
     * 工单状态
     */
    private String workOrderStatus;

    /**
     * 计划开工时间起始
     */
    private LocalDateTime plannedStartTimeStart;

    /**
     * 计划开工时间结束
     */
    private LocalDateTime plannedStartTimeEnd;
}