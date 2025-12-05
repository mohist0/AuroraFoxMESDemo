package com.aurorafox.mesbackend.productionplanning.service;

import com.aurorafox.mesbackend.productionplanning.dto.ProductionOrderCreateRequest;
import com.aurorafox.mesbackend.productionplanning.dto.ProductionOrderDto;
import com.aurorafox.mesbackend.productionplanning.entity.ProductionOrder;
import com.aurorafox.mesbackend.productionplanning.enums.OrderStatus;
import com.aurorafox.mesbackend.productionplanning.repository.ProductionOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ProductionOrderService
 * ----------------------------
 * 生产订单服务层：
 *  - 封装生产订单的业务逻辑
 *  - 提供查询、创建、删除等功能
 */
@Service
public class  ProductionOrderService {

    private final ProductionOrderRepository repository;

    public ProductionOrderService(ProductionOrderRepository repository) {
        this.repository = repository;
    }

    /**
     * 根据订单编号查询生产订单
     * @param id 订单编号
     * @return 生产订单 DTO
     */
    public Optional<ProductionOrderDto> getById(String id) {
        return repository.findById(id).map(this::toDto);
    }

    /**
     * 获取所有生产订单
     * @return 生产订单列表 DTO
     */
    public List<ProductionOrderDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 创建生产订单
     * @param request 创建请求对象
     * @return 创建后的生产订单 DTO
     */
    public ProductionOrderDto create(ProductionOrderCreateRequest request) {
        ProductionOrder order = new ProductionOrder();
        order.setOrderId(request.getOrderId());
        order.setProductId(request.getProductId());
        order.setProductName(request.getProductName());
        order.setOrderQuantity(request.getOrderQuantity());
        order.setOrderStatus(OrderStatus.PENDING_SCHEDULE); // 默认状态
        order.setDeliveryDate(request.getDeliveryDate());
        order.setRemark(request.getRemark());
        repository.save(order);
        return toDto(order);
    }

    /**
     * 删除生产订单
     * @param id 订单编号
     * @return 是否删除成功
     */
    public boolean deleteById(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 实体转 DTO
     * @param order 生产订单实体
     * @return DTO
     */
    private ProductionOrderDto toDto(ProductionOrder order) {
        return ProductionOrderDto.builder()
                .orderId(order.getOrderId())
                .productId(order.getProductId())
                .productName(order.getProductName())
                .orderQuantity(order.getOrderQuantity())
                .scheduledQuantity(order.getScheduledQuantity())
                .completedQuantity(order.getCompletedQuantity())
                .orderStatus(order.getOrderStatus())
                .deliveryDate(order.getDeliveryDate())
                .plannedStartDate(order.getPlannedStartDate())
                .plannedEndDate(order.getPlannedEndDate())
                .remark(order.getRemark())
                .createTime(order.getCreateTime())
                .updateTime(order.getUpdateTime())
                .build();
    }
}
