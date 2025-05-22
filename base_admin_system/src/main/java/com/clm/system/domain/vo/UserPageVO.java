package com.clm.system.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 陈黎明
 * @date 2025/4/29 上午11:28
 */
@Data
public class UserPageVO extends UserVO{
    /**
     * 角色id列表
     */
    private List<Long> roleIds;
}
