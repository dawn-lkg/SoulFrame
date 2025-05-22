package com.clm.web.controller;

import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.domain.OnlineUser;
import com.clm.system.domain.param.OnlineUserQueryParam;
import com.clm.system.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 在线用户监控
 *
 * @author 陈黎明
 * @since 2025-03-11
 */
@RestController
@RequestMapping("/system/online")
@RequiredArgsConstructor
public class OnlineUserController extends BaseController {

    private final OnlineUserService onlineUserService;

    /**
     * 获取在线用户列表
     *
     * @param param 查询参数
     * @return 在线用户列表
     */
    @GetMapping("/list")
    @Log(value = "查询在线用户", businessType = BusinessType.QUERY)
    public Result<List<OnlineUser>> list(OnlineUserQueryParam param) {
        List<OnlineUser> onlineUsers = onlineUserService.listOnlineUsers(param);
        return success(onlineUsers);
    }

    /**
     * 强制退出用户
     *
     * @param tokenId 令牌ID
     * @return 结果
     */
    @DeleteMapping("/{tokenId}")
    @Log(value = "强制退出用户", businessType = BusinessType.FORCE)
    public Result<?> forceLogout(@PathVariable String tokenId) {
        onlineUserService.forceLogout(tokenId);
        return success();
    }
} 