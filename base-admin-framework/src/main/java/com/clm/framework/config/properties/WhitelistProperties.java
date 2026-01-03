package com.clm.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 路由白名单配置
 * @author 陈黎明
 * @date 2025/3/2 上午12:01
 */

@Data
@Component
@ConfigurationProperties(prefix = "router")
public class WhitelistProperties {
    private CopyOnWriteArrayList<String> whitelist = new CopyOnWriteArrayList<>();
}
