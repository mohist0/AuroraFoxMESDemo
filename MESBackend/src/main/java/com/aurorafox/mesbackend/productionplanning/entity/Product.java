package com.aurorafox.mesbackend.productionplanning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 产品信息表实体类，对应数据库表 product
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /**
     * 产品编号，主键
     */
    @Id
    @Column(name = "product_id")
    private String productId;

    /**
     * 产品名称
     */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * 产品说明
     */
    @Column(name = "product_desc")
    private String productDesc;

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
