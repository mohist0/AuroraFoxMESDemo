package com.aurorafox.mesbackend.workorderscheduling.repository;

import com.aurorafox.mesbackend.workorderscheduling.entity.Workorderscheduling;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 工单排程数据访问接口
 * <p>
 * 提供对 production_schedule 表的 CRUD 操作（主键类型为 String）。
 */
public interface WorkorderschedulingRepository extends JpaRepository<Workorderscheduling, String> {
}