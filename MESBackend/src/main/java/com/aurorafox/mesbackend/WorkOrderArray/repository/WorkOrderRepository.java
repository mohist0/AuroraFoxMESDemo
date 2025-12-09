package com.aurorafox.mesbackend.WorkOrderArray.repository;

import com.aurorafox.mesbackend.productionplanning.entity.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单数据访问接口
 * 提供工单表的CRUD及自定义查询操作
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, String> {

    /**
     * 根据生产订单编号查询工单
     * @param orderId 生产订单编号
     * @return 工单列表
     */
    List<WorkOrder> findByOrderId(String orderId);

    /**
     * 根据生产线编号查询工单
     * @param lineId 生产线编号
     * @return 工单列表
     */
    List<WorkOrder> findByLineId(String lineId);

    /**
     * 多条件分页查询工单
     * @param workOrderId 工单编号（模糊）
     * @param orderId 生产订单编号
     * @param productId 产品编号
     * @param lineId 生产线编号
     * @param workOrderStatus 工单状态
     * @param startTime 计划开工时间起始
     * @param endTime 计划开工时间结束
     * @param pageable 分页参数
     * @return 分页工单列表
     */
    @Query("SELECT w FROM WorkOrder w WHERE " +
            "(:#{#workOrderId} IS NULL OR w.workOrderId LIKE %:workOrderId%) AND " +
            "(:#{#orderId} IS NULL OR w.orderId = :orderId) AND " +
            "(:#{#productId} IS NULL OR w.productId = :productId) AND " +
            "(:#{#lineId} IS NULL OR w.lineId = :lineId) AND " +
            "(:#{#workOrderStatus} IS NULL OR w.workOrderStatus = :workOrderStatus) AND " +
            "(:#{#startTime} IS NULL OR w.plannedStartTime >= :startTime) AND " +
            "(:#{#endTime} IS NULL OR w.plannedStartTime <= :endTime)")
    Page<WorkOrder> findByCondition(
            @Param("workOrderId") String workOrderId,
            @Param("orderId") String orderId,
            @Param("productId") String productId,
            @Param("lineId") String lineId,
            @Param("workOrderStatus") String workOrderStatus,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
}