package com.clm.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clm.quartz.domain.entity.SysJobLog;
import com.clm.quartz.service.SysJobLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 调度日志操作处理
 */
@Tag(name = "定时任务调度日志")
@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor/jobLog")
public class JobLogController {

    @Resource
    private SysJobLogService jobLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                    SysJobLog sysJobLog) {
        Page<SysJobLog> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<SysJobLog> queryWrapper = new LambdaQueryWrapper<>();
        if (sysJobLog.getJobName() != null && !sysJobLog.getJobName().isEmpty()) {
            queryWrapper.like(SysJobLog::getJobName, sysJobLog.getJobName());
        }
        if (sysJobLog.getJobGroup() != null && !sysJobLog.getJobGroup().isEmpty()) {
            queryWrapper.eq(SysJobLog::getJobGroup, sysJobLog.getJobGroup());
        }
        if (sysJobLog.getStatus() != null && !sysJobLog.getStatus().isEmpty()) {
            queryWrapper.eq(SysJobLog::getStatus, sysJobLog.getStatus());
        }

        Page<SysJobLog> jobLogPage = jobLogService.page(page, queryWrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", jobLogPage.getRecords());
        result.put("total", jobLogPage.getTotal());
        return result;
    }

    /**
     * 根据调度编号获取详细信息
     */
    @GetMapping(value = "/{jobLogId}")
    public SysJobLog getInfo(@PathVariable Long jobLogId) {
        return jobLogService.getById(jobLogId);
    }

    /**
     * 删除定时任务调度日志
     */
    @DeleteMapping("/{jobLogIds}")
    public Map<String, Object> remove(@PathVariable Long[] jobLogIds) {
        Map<String, Object> result = new HashMap<>();

        boolean success = jobLogService.removeByIds(Arrays.asList(jobLogIds));

        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "操作成功" : "操作失败");
        return result;
    }

    /**
     * 清空定时任务调度日志
     */
    @DeleteMapping("/clean")
    public Map<String, Object> clean() {
        Map<String, Object> result = new HashMap<>();

        jobLogService.cleanJobLog();

        result.put("code", 200);
        result.put("msg", "操作成功");
        return result;
    }
} 