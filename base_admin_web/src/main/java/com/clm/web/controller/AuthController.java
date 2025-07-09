package com.clm.web.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.core.domain.model.LoginBody;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.domain.entity.UserInfo;
import com.clm.system.domain.vo.CaptchaVo;
import com.clm.system.domain.vo.MenuVO;
import com.clm.system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 陈黎明
 * @date 2025/3/1 下午8:59
 */

@Tag(name = "鉴权管理")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<SaTokenInfo> login(@RequestBody @Valid LoginBody loginBody){
        return success(authService.login(loginBody));
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<?> logout(){
        StpUtil.logout(StpUtil.getLoginId());
        return success();
    }

    @Operation(summary = "获取验证码")
    @GetMapping("captcha")
    public Result<CaptchaVo> getCaptcha(){
        return success(authService.createCaptcha());
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/getUserInfo")
    @Log(businessType = BusinessType.QUERY)
    public Result<UserInfo> getUserInfo(){
        return success(authService.getUserInfo());
    }

    @Operation(summary = "获取用户菜单权限")
    @GetMapping("/getRouters")
    @Log(businessType = BusinessType.QUERY)
    public Result<List<MenuVO>> getRouter(){
        return success(authService.getRouter());
    }

}
