package com.aurorafox.mesbackend.productionplanning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 生产订单表实体类，对应数据库表 production_order
 */
@Entity
@Table(name = "production_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionOrder {

    /**
     * 生产订单编号，主键
     */
    @Id
    @Column(name = "order_id")
    private String orderId;

    /**
     * 产品编号，外键关联 product 表
     */
    @Column(name = "product_id", nullable = false)
    private String productId;

    /**
     * 产品名称
     */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * 订单数量
     */
    @Column(name = "order_quantity", nullable = false)
    private Integer orderQuantity;

    /**
     * 已排产数量
     */
    @Column(name = "scheduled_quantity")
    private Integer scheduledQuantity;

    /**
     * 已完成数量
     */
    @Column(name = "completed_quantity")
    private Integer completedQuantity;

    /**
     * 订单状态
     */
    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    /**
     * 交期
     */
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    /**
     * 计划开工日期
     */
    @Column(name = "planned_start_date")
    private LocalDate plannedStartDate;

    /**
     * 计划完工日期
     */
    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 创建时间（数据库自动生成）
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
