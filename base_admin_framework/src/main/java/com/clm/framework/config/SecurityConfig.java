package com.clm.framework.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.clm.framework.config.properties.WhitelistProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 陈黎明
 * @date 2025/3/2 上午12:01
 */
@Slf4j
@Configuration
public class SecurityConfig implements WebMvcConfigurer  {

    @Resource
    private WhitelistProperties whitelistProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("SaTokenConfig.addInterceptors");
        log.info("白名单:{}", whitelistProperties.getWhitelist());
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(whitelistProperties.getWhitelist());
    }

    @Bean
    public FilterRegistrationBean<SaServletFilter> saServletFilter() {
        FilterRegistrationBean<SaServletFilter> registrationBean = new FilterRegistrationBean<>();
        SaServletFilter filter = new SaServletFilter()
                .setBeforeAuth(obj -> {
                    try {
                        log.info("SaServletFilter.setBeforeAuth");
                    } catch (Exception e) {
                        log.error("非Web上下文无法获取响应对象: {}", e.getMessage());
                    }
                });
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/**");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
