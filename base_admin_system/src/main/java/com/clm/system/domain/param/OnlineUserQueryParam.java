package com.clm.system.domain.param;


import com.clm.common.core.domain.BasePageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 在线用户查询参数
 *
 * @author 陈黎明
 * @since 2025-03-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OnlineUserQueryParam extends BasePageParam {
    
    /**
     * 用户账号
     */
    private String userName;
    
    /**
     * IP地址
     */
    private String ipaddr;
} 