package com.aurorafox.mesbackend.usermanagement.controller;

import com.aurorafox.mesbackend.usermanagement.dto.WorkorderschedulingDto;
import com.aurorafox.mesbackend.usermanagement.service.WorkorderschedulingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工单排程 REST 接口
 * <p>
 * 提供工单排程的增删查改端点，路径统一前缀 {@code /api/work-order-schedules}。
 */
@RestController
@RequestMapping("/api/work-order-schedules")
@RequiredArgsConstructor
public class WorkorderschedulingController {

    private final WorkorderschedulingService scheduleService;

    /**
     * 新增工单排程
     *
     * @param dto 排程信息
     * @return 201 Created + 保存后的排程
     */
    @PostMapping
    public ResponseEntity<WorkorderschedulingDto> create(@Valid @RequestBody WorkorderschedulingDto dto) {
        WorkorderschedulingDto created = scheduleService.createSchedule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 根据 ID 查询工单排程
     *
     * @param scheduleId 排程编号
     * @return 200 OK + 排程信息
     */
    @GetMapping("/{scheduleId}")
    public ResponseEntity<WorkorderschedulingDto> getById(@PathVariable String scheduleId) {
        WorkorderschedulingDto dto = scheduleService.getSchedule(scheduleId);
        return ResponseEntity.ok(dto);
    }

    /**
     * 查询全部工单排程
     *
     * @return 200 OK + 排程列表
     */
    @GetMapping
    public ResponseEntity<List<WorkorderschedulingDto>> listAll() {
        List<WorkorderschedulingDto> list = scheduleService.listAllSchedules();
        return ResponseEntity.ok(list);
    }

    /**
     * 更新工单排程
     *
     * @param scheduleId 排程编号（路径变量）
     * @param dto        更新的排程信息
     * @return 200 OK + 更新后的排程
     */
    @PutMapping("/{scheduleId}")
    public ResponseEntity<WorkorderschedulingDto> update(@PathVariable String scheduleId,
                                                         @Valid @RequestBody WorkorderschedulingDto dto) {
        dto.setScheduleId(scheduleId);
        WorkorderschedulingDto updated = scheduleService.updateSchedule(dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除工单排程
     *
     * @param scheduleId 排程编号
     * @return 204 No Content
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> delete(@PathVariable String scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }
}