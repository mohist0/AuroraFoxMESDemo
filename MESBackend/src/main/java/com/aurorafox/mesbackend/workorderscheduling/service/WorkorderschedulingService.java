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
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 工单排程业务逻辑服务
 * <p>
 * 提供创建/查询/更新/删除等业务方法，输入输出使用 DTO 层。
 */
@Service
@RequiredArgsConstructor
public class WorkorderschedulingService {

    private final WorkorderschedulingRepository scheduleRepository;

    /**
     * 新增工单排程
     *
     * @param dto 新建 DTO
     * @return 保存后的响应 DTO
     */
    @Transactional
    public WorkorderschedulingDto createSchedule(WorkorderschedulingCreateDto dto) {
        Workorderscheduling entity = new Workorderscheduling();
        BeanUtils.copyProperties(dto, entity);
        // 若调用方不提供主键则生成 UUID 作为 scheduleId
        if (entity.getScheduleId() == null || entity.getScheduleId().isBlank()) {
            entity.setScheduleId(UUID.randomUUID().toString());
        }
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        Workorderscheduling saved = scheduleRepository.save(entity);
        return toDto(saved);
    }

    /**
     * 根据 ID 查询工单排程
     *
     * @param scheduleId 排程编号
     * @return 排程响应 DTO
     * @throws IllegalArgumentException 排程不存在
     */
    @Transactional(readOnly = true)
    public WorkorderschedulingDto getSchedule(String scheduleId) {
        Workorderscheduling entity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("工单排程不存在: " + scheduleId));
        return toDto(entity);
    }

    /**
     * 查询全部工单排程
     *
     * @return 排程响应 DTO 列表
     */
    @Transactional(readOnly = true)
    public List<WorkorderschedulingDto> listAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 更新工单排程
     *
     * @param dto 更新 DTO（必须包含 scheduleId）
     * @return 更新后的响应 DTO
     * @throws IllegalArgumentException 排程不存在
     */
    @Transactional
    public WorkorderschedulingDto updateSchedule(WorkorderschedulingUpdateDto dto) {
        Workorderscheduling entity = scheduleRepository.findById(dto.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("工单排程不存在: " + dto.getScheduleId()));
        // 复制可更新字段（排除 createTime）
        BeanUtils.copyProperties(dto, entity, "createTime");
        entity.setUpdateTime(LocalDateTime.now());
        Workorderscheduling updated = scheduleRepository.save(entity);
        return toDto(updated);
    }

    /**
     * 删除工单排程
     *
     * @param scheduleId 排程编号
     * @throws IllegalArgumentException 排程不存在
     */
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