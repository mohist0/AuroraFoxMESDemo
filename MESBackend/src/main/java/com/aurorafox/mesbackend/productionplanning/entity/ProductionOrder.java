package com.aurorafox.mesbackend.productionplanning.entity;

import com.aurorafox.mesbackend.productionplanning.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ProductionOrder
 * ----------------------------
 * 生产订单实体类：
 *  - 映射数据库表 production_order
 *  - 包含订单的核心字段和生命周期时间戳
 */
@Entity
@Table(name = "production_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionOrder {

    /** 生产订单编号，主键 */
    @Id
    @Column(name = "order_id")
    private String orderId;

    /** 产品编号 */
    @Column(name = "product_id")
    private String productId;

    /** 产品名称 */
    @Column(name = "product_name")
    private String productName;

    /** 订单数量 */
    @Column(name = "order_quantity")
    private int orderQuantity;

    /** 已排产数量 */
    @Column(name = "scheduled_quantity")
    private Integer scheduledQuantity;

    /** 已完成数量 */
    @Column(name = "completed_quantity")
    private Integer completedQuantity;

    /** 订单状态（枚举约束） */
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    /** 交期 */
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    /** 计划开工日期 */
    @Column(name = "planned_start_date")
    private LocalDate plannedStartDate;

    /** 计划完工日期 */
    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate;

    /** 备注 */
    @Column(name = "remark")
    private String remark;

    /** 创建时间（系统生成） */
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    /** 更新时间（系统更新） */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /** 插入前自动生成创建时间 */
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }

    /** 更新前自动生成更新时间 */
    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
