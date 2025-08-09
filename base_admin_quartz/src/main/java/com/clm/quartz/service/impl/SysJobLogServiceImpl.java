package com.clm.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clm.quartz.domain.entity.SysJobLog;
import com.clm.quartz.mapper.SysJobLogMapper;
import com.clm.quartz.service.SysJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 定时任务调度日志信息 服务实现类
 */
@Service
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements SysJobLogService {

    @Autowired
    private SysJobLogMapper jobLogMapper;

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog() {
        jobLogMapper.cleanJobLog();
    }
} 