package com.clm.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Servlet工具类
 *
 * @author 陈黎明
 * @since 2025-03-08
 */
public class ServletUtils {

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
    
    /**
     * 获取请求参数
     */
    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getParameter(name);
    }
    
    /**
     * 获取请求头
     */
    public static String getHeader(String name) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getHeader(name);
    }
    
    /**
     * 获取User-Agent
     */
    public static String getUserAgent() {
        return getHeader("User-Agent");
    }
} 