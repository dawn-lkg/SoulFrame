package com.clm.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.domain.dto.UserDTO;
import com.clm.system.domain.param.UserQueryParam;
import com.clm.system.domain.vo.UserPageVO;
import com.clm.system.domain.vo.UserVO;
import com.clm.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息表(User)表控制层
 *
 * @author 陈黎明
 * @since 2025-03-07 14:31:36
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
@Validated
public class UserController extends BaseController {
    /**
     * 服务对象
     */
    private final UserService userService;

    /**
     * 分页查询用户列表
     * @param param 查询参数
     * @return 分页结果
     */
    @Operation(summary = "获取用户分页列表")
    @Log(value = "分页查询用户列表", businessType = BusinessType.QUERY)
    @SaCheckPermission("system:user:query")
    @GetMapping("/page")
    public Result<IPage<UserPageVO>> page(@Valid UserQueryParam param) {
        return success(userService.getUserPage(param));
    }

    /**
     * 获取用户详情
     * @param userId 用户ID
     * @return 用户详情
     */
    @Operation(summary = "获取用户详细信息")
    @Log(value = "获取用户详细信息", businessType = BusinessType.QUERY)
    @GetMapping("/{userId}")
    public Result<UserVO> info(@PathVariable Long userId) {
        return success(userService.getUserInfo(userId));
    }

    /**
     * 新增用户
     * @param userDTO 用户参数
     * @return 操作结果
     */
    @Operation(summary = "新增用户")
    @Log(value = "新增用户", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<?> add(@RequestBody @Valid UserDTO userDTO) {
        userService.addUser(userDTO);
        return success();
    }

    /**
     * 修改用户
     * @param userDTO 用户参数
     * @return 操作结果
     */
    @Operation(summary = "修改用户")
    @Log(value = "修改用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<?> update(@RequestBody @Valid UserDTO userDTO) {
        userService.updateUser(userDTO);
        return success();
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 操作结果
     */
    @Operation(summary = "删除用户")
    @Log(value = "删除用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userId}")
    public Result<?> delete(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return success();
    }

    /**
     * 批量删除用户
     * @param userIds 用户ID列表
     * @return 操作结果
     */
    @Log(value = "批量删除用户", businessType = BusinessType.DELETE)
    @Operation(summary = "批量删除用户")
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@RequestBody List<Long> userIds) {
        userService.batchDeleteUser(userIds);
        return success();
    }

    /**
     * 检查用户名是否存在
     * @param username 用户名
     */
    @GetMapping("/checkUsername/{username}")
    public Result<?> checkUsernameExists(@PathVariable String username) {
        return success(userService.checkUsernameExists(username, null));
    }

    /**
     * 重置密码
     * @param userId 用户ID
     */
    @Log(value = "重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPassword/{userId}")
    public Result<?> resetPassword(@PathVariable Long userId) {
        userService.resetPassword(userId);
        return success();
    }
}

