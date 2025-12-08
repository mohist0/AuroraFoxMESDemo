package com.aurorafox.mesbackend.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工单排程数据传输对象（DTO）
 * <p>
 * 用于前后端交互，屏蔽实体细节。
 */
@Data
public class WorkorderschedulingDto {

    /** 排程编号 */
    private String scheduleId;

    /** 工单编号 */
    @NotBlank(message = "工单编号不能为空")
    private String workOrderId;

    /** 生产订单编号 */
    @NotBlank(message = "生产订单编号不能为空")
    private String orderId;

    /** 产品编号 */
    @NotBlank(message = "产品编号不能为空")
    private String productId;

    /** 生产线编号 */
    @NotBlank(message = "生产线编号不能为空")
    private String lineId;

    /** 物料编号 */
    @NotBlank(message = "物料编号不能为空")
    private String materialId;

    /** 计划开工时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedStartTime;

    /** 计划完工时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedEndTime;

    /** 实际开工时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualStartTime;

    /** 实际完工时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualEndTime;

    /** 备注 */
    private String remark;

    /** 创建时间，仅输出 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新时间，仅输出 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;}