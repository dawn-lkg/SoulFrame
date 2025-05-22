package com.clm.system.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.clm.common.core.domain.model.LoginBody;
import com.clm.system.domain.CaptchaVo;
import com.clm.system.domain.UserInfo;
import com.clm.system.domain.vo.MenuVO;

import java.util.List;

/**
 * @author 陈黎明
 * @date 2025/3/4 下午10:17
 */
public interface AuthService {

    /**
     * 登录
     * @param loginBody 登录信息
     * @return token
     */
    SaTokenInfo login(LoginBody loginBody);

    /**
     * 获取验证码
     * @return 验证码
     */
    CaptchaVo createCaptcha();

    /**
     * 获取用户信息
     * @return 用户信息
     */
    UserInfo getUserInfo();

    /**
     * 获取路由
     * @return 路由
     */
    List<MenuVO> getRouter();
}
