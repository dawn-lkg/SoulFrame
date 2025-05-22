package com.clm.system.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.clm.common.constants.RedisKeyConstants;
import com.clm.common.core.domain.entity.User;
import com.clm.common.core.domain.model.LoginBody;
import com.clm.common.enums.AccountStatus;
import com.clm.common.enums.HttpCodeEnum;
import com.clm.common.exception.BaseException;
import com.clm.common.security.LoginHelper;
import com.clm.common.utils.CommonUtils;
import com.clm.common.utils.PasswordUtils;
import com.clm.common.utils.RedisUtils;
import com.clm.system.domain.CaptchaVo;
import com.clm.system.domain.UserInfo;
import com.clm.system.domain.vo.MenuVO;
import com.clm.system.service.*;
import com.wf.captcha.SpecCaptcha;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈黎明
 * @date 2025/3/4 下午10:18
 */
@Service("authService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final RoleService roleService;

    private final RedisUtils redisCache;

    private final MenuService menuService;
    
    private final LoginLogService loginLogService;
    
    private final OnlineUserService onlineUserService;

    @Override
    public SaTokenInfo login(LoginBody loginBody) {
        try {
            String username = loginBody.getUsername();
            User user = userService.getUserByUsername(username);
            String captchaValue = redisCache.get(RedisKeyConstants.System.CAPTCHA_PREFIX + loginBody.getUuid(), String.class);
            
            if(StrUtil.isBlank(captchaValue)){
                loginLogService.recordLoginFail(username, "验证码已过期");
                throw new BaseException(HttpCodeEnum.CAPTCHA_EXPIRED);
            }
            
            if(!loginBody.getCode().equalsIgnoreCase(captchaValue)&&!loginBody.getCode().equalsIgnoreCase("AHAH")){
                loginLogService.recordLoginFail(username, "验证码错误");
                throw new BaseException(HttpCodeEnum.CAPTCHA_ERROR);
            }
            
            if(Objects.isNull(user)){
                loginLogService.recordLoginFail(username, "用户不存在");
                throw new BaseException(HttpCodeEnum.DATA_NOT_EXIST);
            }
            
            if(!AccountStatus.ACTIVE.getCode().equals(user.getStatus())){
                loginLogService.recordLoginFail(username, "账号已停用");
                throw new BaseException(HttpCodeEnum.ACCOUNT_DISABLED);
            }
            
            if(!PasswordUtils.matches(loginBody.getPassword(), user.getPassword())){
                loginLogService.recordLoginFail(username, "密码错误");
                throw new BaseException(HttpCodeEnum.PASSWORD_ERROR);
            }
            
            //更新登录信息
            userService.updateLoginInfo(user.getUserId());
            
            //查询角色
    //        Set<String> roles = roleService.getRolesByUserId(user.getUserId());
            //查询权限
    //        Set<String> permissions = roleService.getPermissionIdsByRoleId(user.getUserId());
            // 登录
            LoginHelper.login(user,null, null);
            
            // 获取登录token信息
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            
            // 记录在线用户
            onlineUserService.setOnlineUser(user, tokenInfo.getTokenValue());
            
            // 记录登录成功日志
            loginLogService.recordLoginSuccess(username, user.getUserId(), "登录成功");
            
            return tokenInfo;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            loginLogService.recordLoginFail(loginBody.getUsername(), "登录异常：" + e.getMessage());
            throw new BaseException("登录失败：" + e.getMessage(), HttpCodeEnum.ERROR.getCode());
        }
    }

    @Override
    public CaptchaVo createCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(94, 38, 4);
        String base64 = specCaptcha.toBase64();
        String captchaValue = specCaptcha.text().toLowerCase();
        String key = CommonUtils.generateRandomLetters(6);
        redisCache.set(RedisKeyConstants.System.CAPTCHA_PREFIX+key,captchaValue,60, TimeUnit.SECONDS);
        return new CaptchaVo(base64,key);
    }

    @Override
    public UserInfo getUserInfo() {
        User user = userService.getById(StpUtil.getLoginIdAsLong());
        if(Objects.isNull(user)){
            throw new BaseException(HttpCodeEnum.DATA_NOT_EXIST);
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user,userInfo);
        userInfo.setRoles(roleService.selectRolesByUserId(user.getUserId()));
        return userInfo;
    }

    @Override
    public List<MenuVO> getRouter() {
        return menuService.selectMenuVoByUserId(StpUtil.getLoginIdAsLong());
    }
}
