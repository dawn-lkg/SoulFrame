package com.clm.web.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.core.domain.model.LoginBody;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author 陈黎明
 * @date 2025/3/1 下午8:59
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginBody loginBody){
        return success(authService.login(loginBody));
    }

    @PostMapping("/logout")
    public Result<?> logout(){
        StpUtil.logout(StpUtil.getLoginId());
        return success();
    }

    @GetMapping("captcha")
    public Result<?> getCaptcha(){
        return success(authService.createCaptcha());
    }

    @GetMapping("/getUserInfo")
    @Log(value = "获取用户信息", businessType = BusinessType.QUERY)
    public Result<?> getUserInfo(){
        return success(authService.getUserInfo());
    }

    @GetMapping("/getRouters")
    @Log(value = "获取用户菜单", businessType = BusinessType.QUERY)
    public Result<?> getRouter(){
        return success(authService.getRouter());
    }

    @GetMapping("/test")
    public Result<?> test(){
        return success("测试成果");
    }


}
