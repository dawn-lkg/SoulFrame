package com.clm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author 陈黎明
 * @date 2025-05-17
 */
@MapperScan("com.clm.**.mapper")
@SpringBootApplication
@EnableConfigurationProperties
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }
}
