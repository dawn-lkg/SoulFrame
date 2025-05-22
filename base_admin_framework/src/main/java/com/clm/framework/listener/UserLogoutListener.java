package com.clm.framework.listener;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.clm.common.utils.RedisUtils;
import com.clm.system.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户登出监听器
 *
 * @author 陈黎明
 * @since 2025-03-11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserLogoutListener implements SaTokenListener {

    private final OnlineUserService onlineUserService;
    private final RedisUtils redisUtils;

//    /**
//     * 每次登录时触发
//     */
//    @Override
//    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
//
//    }

//    @Override
//    public void doLogin(String s, Object o, String s1, SaLoginParameter saLoginParameter) {
//
//    }

    @Override
    public void doLogin(String s, Object o, String s1, SaLoginParameter saLoginParameter) {
        log.debug("用户登录成功，loginId={}, token={}", o, s1);
    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        log.debug("用户注销，loginId={}, token={}", loginId, tokenValue);
        // 删除在线用户信息
        onlineUserService.forceLogout(tokenValue);
    }

    /**
     * 每次被踢下线时触发
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        log.debug("用户被踢下线，loginId={}, token={}", loginId, tokenValue);
        // 删除在线用户信息
        onlineUserService.forceLogout(tokenValue);
    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        log.debug("用户被顶下线，loginId={}, token={}", loginId, tokenValue);
        // 删除在线用户信息
        onlineUserService.forceLogout(tokenValue);
    }

    /**
     * 每次被封禁时触发
     */
    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {
        log.debug("用户被封禁，loginId={}, service={}, level={}, disableTime={}", loginId, service, level, disableTime);
    }

    /**
     * 每次被解封时触发
     */
    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {
        log.debug("用户被解封，loginId={}, service={}", loginId, service);
    }

    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {
        log.debug("开启二级认证，loginType={}, token={}, service={}, safeTime={}", loginType, tokenValue, service, safeTime);
    }

    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {
        log.debug("关闭二级认证，loginType={}, token={}, service={}", loginType, tokenValue, service);
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(String id) {
        log.debug("创建Session，id={}", id);
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(String id) {
        log.debug("注销Session，id={}", id);
    }

    /**
     * 每次Token续期时触发
     */
    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {
        log.debug("Token续期，loginId={}, token={}, timeout={}", loginId, tokenValue, timeout);
    }
} 