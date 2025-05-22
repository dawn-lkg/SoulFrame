package com.clm.system.domain.param;

import com.clm.common.core.domain.BasePageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色查询参数
 *
 * @author 陈黎明
 * @since 2025-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleQueryParam extends BasePageParam {
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色权限字符串
     */
    private String roleKey;
    
    /**
     * 角色状态（0正常 1停用）
     */
    private String status;
} 