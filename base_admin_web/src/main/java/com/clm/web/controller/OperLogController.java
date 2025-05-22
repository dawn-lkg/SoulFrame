package com.clm.web.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.system.domain.param.OperLogQueryParam;
import com.clm.system.domain.vo.OperLogVO;
import com.clm.system.service.OperLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录(OperLog)表控制层
 *
 * @author 陈黎明
 * @since 2025-03-08
 */
@RestController
@RequestMapping("/system/operLog")
@RequiredArgsConstructor
public class OperLogController extends BaseController {
    
    private final OperLogService operLogService;
    
    /**
     * 查询操作日志列表
     */
    @GetMapping("/page")
    public Result<IPage<OperLogVO>> list(OperLogQueryParam param) {
        IPage<OperLogVO> page = operLogService.pageOperLog(param);
        return success(page);
    }
    
    /**
     * 获取操作日志详细信息
     */
    @GetMapping("/{operId}")
    public Result<OperLogVO> getInfo(@PathVariable Long operId) {
        return success(operLogService.getOperLogById(operId));
    }
    
    /**
     * 删除操作日志
     */
    @DeleteMapping("/{operIds}")
    public Result<?> remove(@PathVariable List<Long> operIds) {
        if (operLogService.deleteOperLogByIds(operIds)) {
            return success();
        }
        return error("删除操作日志失败");
    }
    
    /**
     * 清空操作日志
     */
    @DeleteMapping("/clean")
    public Result<?> clean() {
        if (operLogService.cleanOperLog()) {
            return success();
        }
        return error("清空操作日志失败");
    }
    
    /**
     * 导出操作日志
     */
    @GetMapping("/export")
    public Result<List<OperLogVO>> export(OperLogQueryParam param) {
        List<OperLogVO> list = operLogService.exportOperLog(param);
        return success(list);
    }
} 