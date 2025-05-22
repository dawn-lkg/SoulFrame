package com.clm.system.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 操作日志记录视图对象
 *
 * @author 陈黎明
 * @since 2025-03-08
 */
@Data
public class OperLogVO {
    
    /**
     * 日志主键
     */
    private Long operId;
    
    /**
     * 模块标题
     */
    private String title;
    
    /**
     * 业务类型（0查询 1新增 2修改 3删除 4其他）
     */
    private Integer businessType;
    
    /**
     * 业务类型描述
     */
    private String businessTypeDesc;
    
    /**
     * 方法名称
     */
    private String method;
    
    /**
     * 请求方式
     */
    private String requestMethod;
    
    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    private Integer operatorType;
    
    /**
     * 操作类别描述
     */
    private String operatorTypeDesc;
    
    /**
     * 操作人员
     */
    private String operName;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 请求URL
     */
    private String operUrl;
    
    /**
     * 主机地址
     */
    private String operIp;
    
    /**
     * 操作地点
     */
    private String operLocation;
    
    /**
     * 请求参数
     */
    private String operParam;
    
    /**
     * 返回参数
     */
    private String jsonResult;
    
    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;
    
    /**
     * 操作状态描述
     */
    private String statusDesc;
    
    /**
     * 错误消息
     */
    private String errorMsg;
    
    /**
     * 操作时间
     */
    private Date operTime;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 浏览器类型
     */
    private String browser;
    
    /**
     * 耗费时间（毫秒）
     */
    private Long costTime;
    
    /**
     * 格式化后的耗费时间
     */
    private String costTimeDesc;
} 