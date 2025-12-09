package com.aurorafox.mesbackend.WorkOrderArray.controller;

import com.aurorafox.mesbackend.WorkOrderArray.dto.WorkOrderDto;
import com.aurorafox.mesbackend.WorkOrderArray.dto.WorkOrderQueryDto;
import com.aurorafox.mesbackend.productionplanning.entity.WorkOrder;
import com.aurorafox.mesbackend.WorkOrderArray.service.WorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工单管理REST API控制器
 * 提供工单的CRUD接口
 */
@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
@Tag(name = "工单列表", description = "工单的增删查改接口")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    /**
     * 创建新工单
     * @param workOrderDto
     * 工单DTO
     * @return 创建后的工单信息
     */
    @PostMapping
    @Operation(summary = "创建工单", description = "新增一条工单记录")
    public ResponseEntity<WorkOrder> createWorkOrder(@RequestBody WorkOrderDto workOrderDto) {
        WorkOrder workOrder = workOrderService.createWorkOrder(workOrderDto);
        return new ResponseEntity<>(workOrder, HttpStatus.CREATED);
    }

    /**
     * 根据ID删除工单
     * @param workOrderId 工单编号
     * @return 无返回值
     */
    @DeleteMapping("/{workOrderId}")
    @Operation(summary = "删除工单", description = "根据工单编号删除工单记录")
    public ResponseEntity<Void> deleteWorkOrder(@PathVariable String workOrderId) {
        workOrderService.deleteWorkOrder(workOrderId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据ID查询工单
     * @param workOrderId 工单编号
     * @return 工单详情
     */
    @GetMapping("/{workOrderId}")
    @Operation(summary = "查询工单详情", description = "根据工单编号查询工单完整信息")
    public ResponseEntity<WorkOrder> getWorkOrderById(@PathVariable String workOrderId) {
        WorkOrder workOrder = workOrderService.getWorkOrderById(workOrderId);
        return ResponseEntity.ok(workOrder);
    }

    /**
     * 多条件分页查询工单
     * @param queryDTO 查询条件
     * @param page 页码（从0开始）
     * @param size 每页条数
     * @return 分页工单列表
     */
    @PostMapping("/query")
    @Operation(summary = "分页查询工单", description = "根据多条件分页查询工单列表")
    public ResponseEntity<Page<WorkOrder>> queryWorkOrders(
            @RequestBody WorkOrderQueryDto queryDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WorkOrder> workOrders = workOrderService.getWorkOrdersByCondition(queryDTO, pageable);
        return ResponseEntity.ok(workOrders);
    }

    /**
     * 不分页查询全部工单
     * @return 所有工单列表
     */
    @GetMapping("/all")
    @Operation(summary = "查询全部工单（不分页）", description = "无条件查询所有工单记录，返回完整列表")
    public ResponseEntity<List<WorkOrder>> getAllWorkOrdersWithoutPage() {
        List<WorkOrder> workOrders = workOrderService.getAllWorkOrdersWithoutPage();
        return ResponseEntity.ok(workOrders);
    }

    /**
     * 根据生产订单编号查询工单
     * @param orderId 生产订单编号
     * @return 工单列表
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "按生产订单查工单", description = "根据生产订单编号查询关联的所有工单")
    public ResponseEntity<List<WorkOrder>> getWorkOrdersByOrderId(@PathVariable String orderId) {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersByOrderId(orderId);
        return ResponseEntity.ok(workOrders);
    }

    /**
     * 更新工单信息
     * @param workOrderId 工单编号
     * @param workOrderDTO 工单更新信息
     * @return 更新后的工单信息
     */
    @PutMapping("/{workOrderId}")
    @Operation(summary = "更新工单", description = "根据工单编号更新工单信息")
    public ResponseEntity<WorkOrder> updateWorkOrder(
            @PathVariable String workOrderId,
            @RequestBody WorkOrderDto workOrderDTO) {
        WorkOrder updatedWorkOrder = workOrderService.updateWorkOrder(workOrderId, workOrderDTO);
        return ResponseEntity.ok(updatedWorkOrder);
    }
}