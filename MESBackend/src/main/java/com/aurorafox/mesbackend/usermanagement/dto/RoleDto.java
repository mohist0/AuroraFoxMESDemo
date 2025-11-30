package com.aurorafox.mesbackend.usermanagement.dto;

/**
 * 角色数据传输对象 (DTO)，用于角色的增删查改操作。
 */
public class RoleDto {

    private Long id;
    private String name;
    private String description;

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
