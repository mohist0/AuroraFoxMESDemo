package com.aurorafox.mesbackend.workorderscheduling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 生产排程信息表实体类，对应 production_schedule 表
 * <p>
 * 字段映射基于 `src/main/resources/mes_class.sql`，保留数据库层面外键约束的说明（级联/删除行为在数据库中定义）。
 */
@Entity
@Table(name = "production_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workorderscheduling {

    /**
     * 排程编号，主键，对应 schedule_id
     */
    @Id
    @Column(name = "schedule_id", length = 50)
    private String scheduleId;

    /**
     * 工单编号，外键关联 work_order.work_order_id，对应 work_order_id，数据库中为 NOT NULL
     */
    @Column(name = "work_order_id", nullable = false, length = 50)
    private String workOrderId;

    /**
     * 生产订单编号，外键关联 production_order.order_id，对应 order_id，数据库中为 NOT NULL
     */
    @Column(name = "order_id", nullable = false, length = 50)
    private String orderId;

    /**
     * 产品编号，外键关联 product.product_id，对应 product_id，数据库中为 NOT NULL
     */
    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    /**
     * 生产线编号，外键关联 production_line.line_id，对应 line_id，数据库中为 NOT NULL
     */
    @Column(name = "line_id", nullable = false, length = 50)
    private String lineId;

    /**
     * 设备编号，对应 device_id，数据库中允许 NULL；数据库约束为 ON UPDATE CASCADE ON DELETE SET NULL
     */
    @Column(name = "device_id", length = 50)
    private String deviceId;

    /**
     * 物料编号，外键关联 material.material_id，对应 material_id，数据库中为 NOT NULL
     */
    @Column(name = "material_id", nullable = false, length = 50)
    private String materialId;

    /**
     * 计划开工时间，对应 planned_start_time
     */
    @Column(name = "planned_start_time")
    private LocalDateTime plannedStartTime;

    /**
     * 计划完工时间，对应 planned_end_time
     */
    @Column(name = "planned_end_time")
    private LocalDateTime plannedEndTime;

    /**
     * 实际开工时间，对应 actual_start_time
     */
    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    /**
     * 实际完工时间，对应 actual_end_time
     */
    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    /**
     * 备注，对应 remark
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    /**
     * 创建时间，对应 create_time，数据库默认 CURRENT_TIMESTAMP，实体层不可更新或插入该字段
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间，对应 update_time
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
