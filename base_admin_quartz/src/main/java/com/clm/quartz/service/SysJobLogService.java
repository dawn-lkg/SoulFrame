package com.clm.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clm.quartz.domain.entity.SysJobLog;
import org.springframework.stereotype.Service;

/**
 * 定时任务调度日志表 服务接口
 */
@Service("sysJobLogService")
public interface SysJobLogService extends IService<SysJobLog> {

    /**
     * 清空任务日志
     */
    void cleanJobLog();
} 