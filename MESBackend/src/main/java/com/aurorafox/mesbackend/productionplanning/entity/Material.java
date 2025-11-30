package com.aurorafox.mesbackend.productionplanning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 物料信息表实体类，对应数据库表 material
 */
@Entity
@Table(name = "material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    /**
     * 物料编号，主键
     */
    @Id
    @Column(name = "material_id")
    private String materialId;

    /**
     * 物料名称
     */
    @Column(name = "material_name", nullable = false)
    private String materialName;

    /**
     * 物料说明
     */
    @Column(name = "material_desc")
    private String materialDesc;

    /**
     * 计量单位
     */
    @Column(name = "unit")
    private String unit;

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
