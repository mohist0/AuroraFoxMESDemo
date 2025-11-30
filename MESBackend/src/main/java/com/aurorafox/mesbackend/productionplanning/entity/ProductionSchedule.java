package com.aurorafox.mesbackend.productionplanning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 生产排程信息表实体类，对应数据库表 production_schedule
 */
@Entity
@Table(name = "production_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionSchedule {

    /**
     * 排程编号，主键
     */
    @Id
    @Column(name = "schedule_id")
    private String scheduleId;

    /**
     * 工单编号
     */
    @Column(name = "work_order_id", nullable = false)
    private String workOrderId;

    /**
     * 生产订单编号
     */
    @Column(name = "order_id", nullable = false)
    private String orderId;

    /**
     * 产品编号
     */
    @Column(name = "product_id", nullable = false)
    private String productId;

    /**
     * 生产线编号
     */
    @Column(name = "line_id", nullable = false)
    private String lineId;

    /**
     * 物料编号
     */
    @Column(name = "material_id", nullable = false)
    private String materialId;

    /**
     * 计划开工时间
     */
    @Column(name = "planned_start_time")
    private LocalDateTime plannedStartTime;

    /**
     * 计划完工时间
     */
    @Column(name = "planned_end_time")
    private LocalDateTime plannedEndTime;

    /**
     * 实际开工时间
     */
    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    /**
     * 实际完工时间
     */
    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

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
