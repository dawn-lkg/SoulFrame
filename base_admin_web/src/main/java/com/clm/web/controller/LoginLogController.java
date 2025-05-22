package com.clm.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.domain.param.LoginLogQueryParam;
import com.clm.system.domain.vo.LoginLogVO;
import com.clm.system.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问记录(LoginLog)表控制层
 *
 * @author 陈黎明
 * @since 2025-03-10
 */
@RestController
@RequestMapping("/system/loginLog")
@RequiredArgsConstructor
public class LoginLogController extends BaseController {

    private final LoginLogService loginLogService;

    /**
     * 分页查询登录日志
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    @Log(value = "查询登录日志", businessType = BusinessType.QUERY)
    public Result<IPage<LoginLogVO>> page(LoginLogQueryParam param) {
        return success(loginLogService.pageLoginLog(param));
    }

    /**
     * 获取登录日志详情
     *
     * @param infoId 访问ID
     * @return 登录日志详情
     */
    @GetMapping("/{infoId}")
    @Log(value = "查询登录日志详情", businessType = BusinessType.QUERY)
    public Result<LoginLogVO> info(@PathVariable Long infoId) {
        return success(loginLogService.getLoginLogById(infoId));
    }

    /**
     * 删除登录日志
     * @param infoId 访问ID
     * @return 操作结果
     */
    @DeleteMapping("/{infoId}")
    @Log(value = "删除登录日志", businessType = BusinessType.DELETE)
    public Result<?> delete(@PathVariable Long infoId) {
        loginLogService.removeById(infoId);
        return success();
    }

    /**
     * 删除登录日志
     *
     * @param infoIds 访问ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    @Log(value = "删除登录日志", businessType = BusinessType.DELETE)
    public Result<?> delete(@RequestBody List<Long> infoIds) {
        loginLogService.deleteLoginLogByIds(infoIds);
        return success();
    }

    /**
     * 清空登录日志
     *
     * @return 操作结果
     */
    @DeleteMapping("/clean")
    @Log(value = "清空登录日志", businessType = BusinessType.CLEAN)
    public Result<?> clean() {
        loginLogService.cleanLoginLog();
        return success();
    }
} 