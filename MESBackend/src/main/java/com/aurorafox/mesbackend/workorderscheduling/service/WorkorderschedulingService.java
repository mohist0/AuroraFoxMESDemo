package com.aurorafox.mesbackend.workorderscheduling.service;

import com.aurorafox.mesbackend.workorderscheduling.dto.WorkorderschedulingCreateDto;
import com.aurorafox.mesbackend.workorderscheduling.dto.WorkorderschedulingDto;
import com.aurorafox.mesbackend.workorderscheduling.dto.WorkorderschedulingUpdateDto;
import com.aurorafox.mesbackend.workorderscheduling.entity.Workorderscheduling;
import com.aurorafox.mesbackend.workorderscheduling.repository.WorkorderschedulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// 工单排程业务逻辑服务，提供创建/查询/更新/删除等业务方法
@Service
@RequiredArgsConstructor
public class WorkorderschedulingService {

    private final WorkorderschedulingRepository scheduleRepository;

    // 新增工单排程，必须由调用方提供 scheduleId
    @Transactional
    public WorkorderschedulingDto createSchedule(WorkorderschedulingCreateDto dto) {
        Workorderscheduling entity = new Workorderscheduling();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getScheduleId() == null || entity.getScheduleId().isBlank()) {
            throw new IllegalArgumentException("创建排程时必须提供 scheduleId");
        }
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        Workorderscheduling saved = scheduleRepository.save(entity);
        return toDto(saved);
    }

    // 根据 ID 查询工单排程
    @Transactional(readOnly = true)
    public WorkorderschedulingDto getSchedule(String scheduleId) {
        Workorderscheduling entity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("工单排程不存在: " + scheduleId));
        return toDto(entity);
    }

    // 查询全部工单排程
    @Transactional(readOnly = true)
    public List<WorkorderschedulingDto> listAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 更新工单排程，排除 scheduleId 和 createTime，不允许更新主键
    @Transactional
    public WorkorderschedulingDto updateSchedule(String scheduleId, WorkorderschedulingUpdateDto dto) {
        Workorderscheduling entity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("工单排程不存在: " + scheduleId));
        BeanUtils.copyProperties(dto, entity, "scheduleId", "createTime");
        entity.setUpdateTime(LocalDateTime.now());
        Workorderscheduling updated = scheduleRepository.save(entity);
        return toDto(updated);
    }

    // 删除工单排程
    @Transactional
    public void deleteSchedule(String scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new IllegalArgumentException("工单排程不存在: " + scheduleId);
        }
        scheduleRepository.deleteById(scheduleId);
    }

    private WorkorderschedulingDto toDto(Workorderscheduling entity) {
        WorkorderschedulingDto dto = new WorkorderschedulingDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
