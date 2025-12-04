package com.aurorafox.mesbackend.productionplanning.dto;

import com.aurorafox.mesbackend.productionplanning.enums.OrderStatus;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ProductionOrderDto
 * ----------------------------
 * 生产订单响应 DTO：
 *  - 用于返回生产订单信息给前端
 *  - 包含数据库中的完整字段
 */
@Data
@Builder
public class ProductionOrderDto {
    private String orderId;            // 生产订单编号
    private String productId;          // 产品编号
    private String productName;        // 产品名称
    private int orderQuantity;         // 订单数量
    private Integer scheduledQuantity; // 已排产数量（可为空）
    private Integer completedQuantity; // 已完成数量（可为空）
    private OrderStatus orderStatus;   // 订单状态（枚举）
    private LocalDate deliveryDate;    // 交期（可为空）
    private LocalDate plannedStartDate;// 计划开工日期（可为空）
    private LocalDate plannedEndDate;  // 计划完工日期（可为空）
    private String remark;             // 备注（可为空）
    private LocalDateTime createTime;  // 创建时间（系统生成）
    private LocalDateTime updateTime;  // 更新时间（可为空）
}
