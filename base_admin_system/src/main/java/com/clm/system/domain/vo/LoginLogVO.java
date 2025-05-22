package com.clm.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 系统访问记录视图对象
 *
 * @author 陈黎明
 * @since 2025-03-10
 */
@Data
public class LoginLogVO {
    
    /**
     * 访问ID
     */
    private Long infoId;
    
    /**
     * 用户账号
     */
    private String userName;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 登录IP地址
     */
    private String ipaddr;
    
    /**
     * 登录地点
     */
    private String loginLocation;
    
    /**
     * 浏览器类型
     */
    private String browser;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 登录状态（0成功 1失败）
     */
    private String status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 提示消息
     */
    private String msg;
    
    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;
} 