package com.aurorafox.mesbackend.usermanagement.service;

import com.aurorafox.mesbackend.usermanagement.dto.WorkorderschedulingDto;
import com.aurorafox.mesbackend.usermanagement.entity.Workorderscheduling;
import com.aurorafox.mesbackend.usermanagement.repository.WorkorderschedulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工单排程业务逻辑服务
 */
@Service
@RequiredArgsConstructor
public class WorkorderschedulingService {

    private final WorkorderschedulingRepository scheduleRepository;

    /**
     * 新增工单排程
     *
     * @param dto 排程 DTO
     * @return 保存后的排程 DTO
     */
    @Transactional
    public WorkorderschedulingDto createSchedule(WorkorderschedulingDto dto) {
        Workorderscheduling entity = new Workorderscheduling();
        BeanUtils.copyProperties(dto, entity);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        Workorderscheduling saved = scheduleRepository.save(entity);
        return toDto(saved);
    }

    /**
     * 根据 ID 查询工单排程
     *
     * @param scheduleId 排程编号
     * @return 排程 DTO
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
     * @return 排程 DTO 列表
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
     * @param dto 排程 DTO（必须包含 scheduleId）
     * @return 更新后的排程 DTO
     * @throws IllegalArgumentException 排程不存在
     */
    @Transactional
    public WorkorderschedulingDto updateSchedule(WorkorderschedulingDto dto) {
        Workorderscheduling entity = scheduleRepository.findById(dto.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("工单排程不存在: " + dto.getScheduleId()));
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

    /* ---------- 私有工具方法 ---------- */

    private WorkorderschedulingDto toDto(Workorderscheduling entity) {
        WorkorderschedulingDto dto = new WorkorderschedulingDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}