package com.aurorafox.mesbackend.usermanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户信息表实体类，对应数据库表 users
 */

@Data
@Entity // 标记该类为 JPA 实体类，会映射到数据库表
@Table(name = "users") // 指定映射的数据库表名
@Getter // Lombok 自动生成所有字段的 getter 方法
@Setter // Lombok 自动生成所有字段的 setter 方法
@NoArgsConstructor // Lombok 自动生成无参构造函数
@AllArgsConstructor // Lombok 自动生成包含所有字段的构造函数
@Builder // Lombok 自动生成 Builder 模式，方便链式创建对象
public class User {

    /**
     * 用户编号，主键
     */
    @Id // 指定主键
    @Column(name = "user_id") // 指定对应的数据库列名
    private String userId;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户密码
     */
    @Column(name = "user_password")
    private String userPassword;

    /**
     * 盐值
     */
    @Column(name = "salt_value")
    private String saltValue;

    /**
     * 最近登录时间
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间（数据库自动生成，禁止手动插入或更新）
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private LocalDateTime createTime;


}
