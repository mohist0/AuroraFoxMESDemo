package com.aurorafox.mesbackend.productionplanning.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * ProductionOrderCreateRequest
 * ----------------------------
 * 生产订单创建请求 DTO：
 *  - 用于接收前端传入的订单创建数据
 *  - 不包含系统生成字段（如状态、时间戳）
 */
@Data
public class ProductionOrderCreateRequest {
    private String orderId;         // 生产订单编号
    private String productId;       // 产品编号
    private String productName;     // 产品名称
    private int orderQuantity;      // 订单数量
    private LocalDate deliveryDate; // 交期（可为空）
    private String remark;          // 备注（可为空）
}
