package com.aurorafox.mesbackend.WorkOrderArray.service;

import com.aurorafox.mesbackend.WorkOrderArray.dto.WorkOrderDto;
import com.aurorafox.mesbackend.WorkOrderArray.dto.WorkOrderQueryDto;
import com.aurorafox.mesbackend.productionplanning.entity.WorkOrder;
import com.aurorafox.mesbackend.WorkOrderArray.repository.WorkOrderRepository;
import org.springframework.data.domain.Page;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单业务逻辑层
 * 封装工单的CRUD及业务规则处理
 */
@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;

    /**
     * 创建新工单
     * @param workOrderDto 工单DTO
     * @return 创建后的工单实体
     */
    @Transactional
    public WorkOrder createWorkOrder(WorkOrderDto workOrderDto) {
        WorkOrder workOrder = new WorkOrder();
        copyDtoToEntity(workOrderDto, workOrder);
        workOrder.setCreateTime(LocalDateTime.now());
        return workOrderRepository.save(workOrder);
    }

    /**
     * 根据ID删除工单
     * @param workOrderId 工单编号
     */
    @Transactional
    public void deleteWorkOrder(String workOrderId) {
        if (!workOrderRepository.existsById(workOrderId)) {
            throw new EntityNotFoundException("工单不存在：" + workOrderId);
        }
        workOrderRepository.deleteById(workOrderId);
    }

    /**
     * 根据ID查询工单
     * @param workOrderId 工单编号
     * @return 工单实体
     */
    public WorkOrder getWorkOrderById(String workOrderId) {
        return workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new EntityNotFoundException("工单不存在：" + workOrderId));
    }

    /**
     * 分页查询工单（多条件）
     * @param queryDTO 查询条件DTO
     * @param pageable 分页参数
     * @return 分页工单列表
     */
    public Page<WorkOrder> getWorkOrdersByCondition(WorkOrderQueryDto queryDTO, Pageable pageable) {
        return workOrderRepository.findByCondition(
                queryDTO.getWorkOrderId(),
                queryDTO.getOrderId(),
                queryDTO.getProductId(),
                queryDTO.getLineId(),
                queryDTO.getWorkOrderStatus(),
                queryDTO.getPlannedStartTimeStart(),
                queryDTO.getPlannedStartTimeEnd(),
                pageable
        );
    }

    /**
     * 查询全部工单（不分页）
     * @return 所有工单列表
     */
    public List<WorkOrder> getAllWorkOrdersWithoutPage() {
        return workOrderRepository.findAll();
    }


    /**
     * 根据生产订单编号查询工单
     * @param orderId 生产订单编号
     * @return 工单列表
     */
    public List<WorkOrder> getWorkOrdersByOrderId(String orderId) {
        return workOrderRepository.findByOrderId(orderId);
    }

    /**
     * 更新工单信息
     * @param workOrderId 工单编号
     * @param workOrderDTO 工单DTO
     * @return 更新后的工单实体
     */
    @Transactional
    public WorkOrder updateWorkOrder(String workOrderId, WorkOrderDto workOrderDTO) {
        WorkOrder existingWorkOrder = getWorkOrderById(workOrderId);
        copyDtoToEntity(workOrderDTO, existingWorkOrder);
        existingWorkOrder.setUpdateTime(LocalDateTime.now());
        return workOrderRepository.save(existingWorkOrder);
    }

    /**
     * 将DTO属性复制到实体类
     * @param dto DTO对象
     * @param entity 实体对象
     */
    private void copyDtoToEntity(WorkOrderDto dto, WorkOrder entity) {
        entity.setWorkOrderId(dto.getWorkOrderId());
        entity.setOrderId(dto.getOrderId());
        entity.setProductId(dto.getProductId());
        entity.setQuantity(dto.getQuantity());
        entity.setScheduledQuantity(dto.getScheduledQuantity());
        entity.setCompletedQuantity(dto.getCompletedQuantity());
        entity.setWorkOrderStatus(dto.getWorkOrderStatus());
        entity.setPlannedStartTime(dto.getPlannedStartTime());
        entity.setPlannedEndTime(dto.getPlannedEndTime());
        entity.setActualStartTime(dto.getActualStartTime());
        entity.setActualEndTime(dto.getActualEndTime());
        entity.setLineId(dto.getLineId());
        entity.setRemark(dto.getRemark());
    }
}
