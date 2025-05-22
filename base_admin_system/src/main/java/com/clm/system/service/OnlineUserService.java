package com.clm.system.service;

import com.clm.common.core.domain.entity.User;
import com.clm.system.domain.OnlineUser;
import com.clm.system.domain.param.OnlineUserQueryParam;

import java.util.List;

/**
 * 在线用户服务接口
 *
 * @author 陈黎明
 * @since 2025-03-11
 */
public interface OnlineUserService {
    
    /**
     * 查询在线用户列表
     *
     * @param param 查询参数
     * @return 在线用户列表
     */
    List<OnlineUser> listOnlineUsers(OnlineUserQueryParam param);
    
    /**
     * 强制退出用户
     *
     * @param tokenId 令牌ID
     * @return 结果
     */
    boolean forceLogout(String tokenId);
    
    /**
     * 设置用户信息到缓存
     *
     * @param user 用户信息
     * @param tokenId 令牌ID
     * @param loginTime 登录时间
     */
    void setOnlineUser(User user, String tokenId);
    
    /**
     * 获取单个在线用户
     *
     * @param tokenId 令牌ID
     * @return 在线用户
     */
    OnlineUser getOnlineUser(String tokenId);
} 