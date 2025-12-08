package com.aurorafox.mesbackend.workorderscheduling.controller;

import com.aurorafox.mesbackend.workorderscheduling.dto.WorkorderschedulingCreateDto;
import com.aurorafox.mesbackend.workorderscheduling.dto.WorkorderschedulingDto;
import com.aurorafox.mesbackend.workorderscheduling.dto.WorkorderschedulingUpdateDto;
import com.aurorafox.mesbackend.workorderscheduling.service.WorkorderschedulingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 工单排程 REST 接口，提供增删查改功能
@Tag(name = "工单排程接口", description = "提供工单排程的增删查改功能")
@RestController
@RequestMapping("/api/workorderscheduling")
@RequiredArgsConstructor
public class WorkorderschedulingController {

    private final WorkorderschedulingService scheduleService;

    @Operation(summary = "创建工单排程", description = "创建一个新的工单排程并返回保存后的排程信息", security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping("/add")
    public ResponseEntity<WorkorderschedulingDto> create(@Valid @RequestBody WorkorderschedulingCreateDto dto) {
        WorkorderschedulingDto created = scheduleService.createSchedule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "根据ID获取工单排程", description = "通过排程编号查询单个工单排程信息", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/getById/{schedule_id}")
    public ResponseEntity<WorkorderschedulingDto> getById(@PathVariable("schedule_id") String scheduleId) {
        WorkorderschedulingDto dto = scheduleService.getSchedule(scheduleId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "获取所有工单排程", description = "查询系统中所有的工单排程列表", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/getAll")
    public ResponseEntity<List<WorkorderschedulingDto>> listAll() {
        List<WorkorderschedulingDto> list = scheduleService.listAllSchedules();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "更新工单排程", description = "根据排程编号更新工单排程信息，返回更新后的排程", security = {@SecurityRequirement(name = "bearerAuth")})
    @PutMapping("/update/{schedule_id}")
    public ResponseEntity<WorkorderschedulingDto> update(@PathVariable("schedule_id") String scheduleId,
                                                         @Valid @RequestBody WorkorderschedulingUpdateDto dto) {
        WorkorderschedulingDto updated = scheduleService.updateSchedule(scheduleId, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "删除工单排程", description = "根据排程编号删除工单排程，删除成功返回 204 No Content", security = {@SecurityRequirement(name = "bearerAuth")})
    @DeleteMapping("/delete/{schedule_id}")
    public ResponseEntity<Void> delete(@PathVariable("schedule_id") String scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
