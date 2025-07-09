package com.clm.web.controller;

import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.domain.entity.OnlineUser;
import com.clm.system.domain.param.OnlineUserQueryParam;
import com.clm.system.service.OnlineUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 在线用户监控
 *
 * @author 陈黎明
 * @since 2025-03-11
 */

@Tag(name = "在线用户管理")
@RestController
@RequestMapping("/system/online")
@RequiredArgsConstructor
public class OnlineUserController extends BaseController {

    private final OnlineUserService onlineUserService;

    @Operation(summary = "获取在线用户列表")
    @GetMapping("/list")
    @Log(businessType = BusinessType.QUERY)
    public Result<List<OnlineUser>> list(OnlineUserQueryParam param) {
        List<OnlineUser> onlineUsers = onlineUserService.listOnlineUsers(param);
        return success(onlineUsers);
    }

    @Operation(summary = "强制退出用户")
    @DeleteMapping("/{tokenId}")
    @Log(businessType = BusinessType.FORCE)
    public Result<?> forceLogout(@PathVariable String tokenId) {
        onlineUserService.forceLogout(tokenId);
        return success();
    }

    @Operation(summary = "批量退出用户")
    @DeleteMapping("batch")
    @Log(businessType = BusinessType.FORCE)
    public Result<?> forceLogout(@RequestBody List<String> tokenIds) {
        tokenIds.forEach(onlineUserService::forceLogout);
        return success();
    }
} 