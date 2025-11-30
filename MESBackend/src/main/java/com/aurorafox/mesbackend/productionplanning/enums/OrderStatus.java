package com.aurorafox.mesbackend.productionplanning.enums;

/**
 * 生产订单状态枚举
 */
public enum OrderStatus {
    PENDING_SCHEDULE, // 待排程
    SCHEDULED,        // 已排程
    IN_PROGRESS,      // 生产中
    COMPLETED         // 已完成
}
