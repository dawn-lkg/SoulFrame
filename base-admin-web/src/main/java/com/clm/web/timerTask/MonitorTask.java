package com.clm.web.timerTask;

import cn.hutool.core.date.DateUtil;
import com.clm.common.constants.RedisKeyConstants;
import com.clm.common.exception.BaseException;
import com.clm.common.utils.RedisUtils;
import com.clm.system.constants.PerformanceMetricsConstants;
import com.clm.system.domain.dto.MonitorDataDTO;
import com.clm.system.domain.entity.PerformanceMetrics;
import com.clm.system.domain.entity.Server;
import com.clm.system.service.PerformanceMetricsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈黎明
 * @since 2025-03-08
 */
@Slf4j
@Component("monitorTask")
public class MonitorTask {

    @Resource
    private PerformanceMetricsService performanceMetricsService;

    @Resource
    private RedisUtils redisUtils;

    public void runTask1() {
        System.out.println("执行静态定时任务时间: " + System.currentTimeMillis());
    }

    /**
     * 记录cpu使用率
     */
    public void recordCpuUsage() {
        Server server = new Server();
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        server.setCpuInfo(hardware.getProcessor());
        PerformanceMetrics performanceMetrics = new PerformanceMetrics();
        performanceMetrics.setMetricType(PerformanceMetricsConstants.METRIC_TYPE_CPU_USAGE);
        performanceMetrics.setMetricValue(server.getCpu().getUsed());
        performanceMetrics.setCollectTime(new Date());
        if (!performanceMetricsService.save(performanceMetrics)) {
            throw new BaseException("保存CPU使用率失败");
        }
    }

    /**
     * 实时CPU使用率
     */
    public void realTimeCpuUsage() {
        Server server = new Server();
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        server.setCpuInfo(hardware.getProcessor());
        String used = String.valueOf(server.getCpu().getUsed());
        String time = DateUtil.format(new Date(), "ss");
        redisUtils.enqueue(RedisKeyConstants.Monitor.CPU_USAGE, new MonitorDataDTO(time, used), 30000, TimeUnit.SECONDS);
        long size = redisUtils.size(RedisKeyConstants.Monitor.CPU_USAGE);
        if (size > 60) {
            redisUtils.dequeue(RedisKeyConstants.Monitor.CPU_USAGE);
        }
        PerformanceMetrics performanceMetrics = new PerformanceMetrics();
        performanceMetrics.setMetricType(PerformanceMetricsConstants.METRIC_TYPE_CPU_USAGE);
        performanceMetrics.setMetricValue(server.getCpu().getUsed());
        performanceMetrics.setCollectTime(new Date());
        if (!performanceMetricsService.save(performanceMetrics)) {
            throw new BaseException("保存CPU使用率失败");
        }
    }

    /**
     * 实时内存使用率
     */
    public void realTimeMemUsage() {
        Server server = new Server();
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        server.setMemInfo(hardware.getMemory());
        String used = String.valueOf(server.getMem().getUsage());
        String time = DateUtil.format(new Date(), "ss");
        redisUtils.enqueue(RedisKeyConstants.Monitor.MEM_USAGE, new MonitorDataDTO(time, used), 30000, TimeUnit.SECONDS);
        long size = redisUtils.size(RedisKeyConstants.Monitor.MEM_USAGE);
        if (size > 60) {
            redisUtils.dequeue(RedisKeyConstants.Monitor.MEM_USAGE);
        }
        PerformanceMetrics performanceMetrics = new PerformanceMetrics();
        performanceMetrics.setMetricType(PerformanceMetricsConstants.METRIC_TYPE_MEM_USAGE);
        performanceMetrics.setMetricValue(server.getMem().getUsage());
        performanceMetrics.setCollectTime(new Date());
        if (!performanceMetricsService.save(performanceMetrics)) {
            throw new BaseException("保存内存使用率失败");
        }
    }
}
