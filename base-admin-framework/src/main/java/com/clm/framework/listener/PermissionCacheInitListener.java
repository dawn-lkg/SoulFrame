package com.clm.framework.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 权限缓存初始化监听器
 * 在应用启动后加载权限数据到缓存
 *
 * @author 陈黎明
 * @date 2025/3/10
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@Order(1)
public class PermissionCacheInitListener implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        log.info("应用启动完成，开始初始化权限缓存...");
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                // TODO 缓存权限数据
            } catch (Exception e) {
                log.error("权限缓存初始化异常", e);
            }
        }, "permission-cache-init-thread").start();
    }
} 