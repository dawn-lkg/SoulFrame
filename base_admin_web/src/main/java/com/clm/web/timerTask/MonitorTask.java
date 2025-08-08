package com.clm.web.timerTask;

import org.springframework.stereotype.Component;

/**
 * @author 陈黎明
 * @since 2025-03-08
 */

@Component("monitorTask")
public class MonitorTask {

    public void runTask1() {
        System.out.println("执行静态定时任务时间: " + System.currentTimeMillis());
    }
}
