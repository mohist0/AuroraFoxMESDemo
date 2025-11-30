package com.aurorafox.mesbackend.productionplanning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 生产线信息表实体类，对应数据库表 production_line
 */
@Entity
@Table(name = "production_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionLine {

    /**
     * 生产线编号，主键
     */
    @Id
    @Column(name = "line_id")
    private String lineId;

    /**
     * 生产线名称
     */
    @Column(name = "line_name", nullable = false)
    private String lineName;

    /**
     * 生产线说明
     */
    @Column(name = "line_desc")
    private String lineDesc;

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
