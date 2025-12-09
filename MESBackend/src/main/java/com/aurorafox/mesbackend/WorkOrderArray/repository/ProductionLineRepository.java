package com.aurorafox.mesbackend.WorkOrderArray.repository;

import com.aurorafox.mesbackend.productionplanning.entity.ProductionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 生产线数据访问接口
 * 提供生产线表的CRUD操作
 */
@Repository
public interface ProductionLineRepository extends JpaRepository<ProductionLine, String> {
}