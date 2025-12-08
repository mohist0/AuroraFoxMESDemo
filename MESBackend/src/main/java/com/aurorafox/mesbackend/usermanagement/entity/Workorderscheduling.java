package com.aurorafox.mesbackend.usermanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 工单排程信息表实体类，对应 production_schedule 表
 */
@Entity
@Table(name = "production_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workorderscheduling {

    /** 排程编号，主键 */
    @Id
    @Column(name = "schedule_id")
    private String scheduleId;

    /** 工单编号，外键关联 work_order.work_order_id */
    @Column(name = "work_order_id", nullable = false)
    private String workOrderId;

    /** 生产订单编号，外键关联 production_order.order_id */
    @Column(name = "order_id", nullable = false)
    private String orderId;

    /** 产品编号，外键关联 product.product_id */
    @Column(name = "product_id", nullable = false)
    private String productId;

    /** 生产线编号，外键关联 production_line.line_id */
    @Column(name = "line_id", nullable = false)
    private String lineId;

    /** 物料编号，外键关联 material.material_id */
    @Column(name = "material_id", nullable = false)
    private String materialId;

    /** 计划开工时间 */
    @Column(name = "planned_start_time")
    private LocalDateTime plannedStartTime;

    /** 计划完工时间 */
    @Column(name = "planned_end_time")
    private LocalDateTime plannedEndTime;

    /** 实际开工时间 */
    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    /** 实际完工时间 */
    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    /** 备注 */
    @Column(name = "remark")
    private String remark;

    /** 创建时间 */
    @Column(name = "create_time", insertable = false, updatable = false)
    private LocalDateTime createTime;

    /** 更新时间 */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}