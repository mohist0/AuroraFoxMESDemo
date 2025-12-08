package com.aurorafox.mesbackend.usermanagement.repository;

import com.aurorafox.mesbackend.usermanagement.entity.Workorderscheduling;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 工单排程数据访问接口
 */
public interface WorkorderschedulingRepository extends JpaRepository<Workorderscheduling, String> {
}