package com.aurorafox.mesbackend.productionplanning.controller;

import com.aurorafox.mesbackend.productionplanning.dto.ProductionOrderCreateRequest;
import com.aurorafox.mesbackend.productionplanning.dto.ProductionOrderDto;
import com.aurorafox.mesbackend.productionplanning.service.ProductionOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductionOrderController
 * ----------------------------
 * 生产订单接口控制器：
 *  - 提供生产订单的创建、查询、删除功能
 */
@RestController
@RequestMapping("/api/productionorder")
@Tag(name = "生产订单接口", description = "提供生产订单相关功能")
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    public ProductionOrderController(ProductionOrderService productionOrderService) {
        this.productionOrderService = productionOrderService;
    }

    /**
     * 根据订单编号查询生产订单
     * @param id 订单编号
     * @return 生产订单信息
     */
    @Operation(summary = "根据订单编号查询生产订单")
    @Tag(name = "查询接口", description = "根据订单编号查询生产订单")
    @GetMapping("/getProductionOrderById/{id}")
    public ResponseEntity<ProductionOrderDto> getById(@PathVariable String id) {
        return productionOrderService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取所有生产订单
     * @return 生产订单列表
     */
    @Operation(summary = "获取所有生产订单")
    @Tag(name = "查询接口", description = "获取所有生产订单")
    @GetMapping("/getAllProductionOrders")
    public ResponseEntity<List<ProductionOrderDto>> getAll() {
        return ResponseEntity.ok(productionOrderService.getAll());
    }

    /**
     * 创建生产订单
     * @param request 创建请求对象
     * @return 创建后的生产订单信息
     */
    @Operation(summary = "创建生产订单")
    @Tag(name = "新增接口", description = "创建新的生产订单")
    @PostMapping("/addProductionOrder")
    public ResponseEntity<ProductionOrderDto> create(@RequestBody ProductionOrderCreateRequest request) {
        return ResponseEntity.ok(productionOrderService.create(request));
    }

    /**
     * 删除生产订单
     * @param id 订单编号
     * @return 删除结果
     */
    @Operation(summary = "删除生产订单")
    @Tag(name = "删除接口", description = "根据订单编号删除生产订单")
    @DeleteMapping("/deleteProductionOrder/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = productionOrderService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
