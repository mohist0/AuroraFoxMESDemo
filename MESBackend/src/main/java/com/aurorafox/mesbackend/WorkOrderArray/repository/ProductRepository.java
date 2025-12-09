package com.aurorafox.mesbackend.WorkOrderArray.repository;

import com.aurorafox.mesbackend.productionplanning.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 产品数据访问接口
 * 提供产品表的CRUD操作
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
