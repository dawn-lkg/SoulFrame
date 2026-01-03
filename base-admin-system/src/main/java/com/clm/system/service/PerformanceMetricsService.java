package com.clm.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clm.system.domain.entity.PerformanceMetrics;
import com.clm.system.domain.param.PerformanceMetricsParam;
import com.clm.system.domain.vo.PerformanceMetricsDayVO;
import com.clm.system.domain.vo.PerformanceMetricsHourVO;
import com.clm.system.domain.vo.PerformanceMetricsRealTimeVO;

import java.util.List;


/**
 * 性能监控 服务层
 *
 * @author 陈黎明
 * @date 2025-08-19 12:30:16
 */
public interface PerformanceMetricsService extends IService<PerformanceMetrics> {

    /**
     * 实时数据
     *
     * @return PerformanceMetricsRealTimeVO
     */
    PerformanceMetricsRealTimeVO realtime();

    /**
     * 按小时查询性能监控列表
     *
     * @param param
     * @return List<PerformanceMetricsHourVO>
     */
    List<PerformanceMetricsHourVO> selectPerformanceMetricsHour(PerformanceMetricsParam param);

    /**
     * 按天查询性能监控列表
     *
     * @param param
     * @return Page<PerformanceMetricsHourVO>
     */
    List<PerformanceMetricsDayVO> selectPerformanceMetricsDay(PerformanceMetricsParam param);

    /**
     * 按周查询性能监控列表
     *
     * @param param
     * @return Page<PerformanceMetricsHourVO>
     */
    List<PerformanceMetricsDayVO> selectPerformanceMetricsWeek(PerformanceMetricsParam param);
}
