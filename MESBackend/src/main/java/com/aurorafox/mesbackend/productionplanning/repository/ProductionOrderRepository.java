package com.aurorafox.mesbackend.productionplanning.repository;

import com.aurorafox.mesbackend.productionplanning.entity.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ProductionOrderRepository
 * ----------------------------
 * 生产订单仓库接口：
 *  - 提供生产订单的数据库访问方法
 *  - 继承 JpaRepository，自动具备常用 CRUD 功能
 */
@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, String> {
}
