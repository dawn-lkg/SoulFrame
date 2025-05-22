package com.clm.system.domain.param;


import com.clm.common.core.domain.BasePageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询参数
 *
 * @author 陈黎明
 * @since 2025-03-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryParam extends BasePageParam {
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 昵称
     */
    private String nickName;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 状态（0正常 1停用）
     */
    private String status;
    
    /**
     * 部门ID
     */
    private Long deptId;
} 