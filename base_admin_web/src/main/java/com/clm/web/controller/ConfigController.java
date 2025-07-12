package com.clm.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.domain.dto.ConfigDTO;
import com.clm.system.domain.param.ConfigParam;
import com.clm.system.domain.vo.ConfigVO;
import com.clm.system.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置表 控制层
 *
 * @author 陈黎明
 * @date 2025-07-12 19:54:30
 */
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
@Validated
@Tag(name = "系统配置表管理", description = "系统配置表相关接口")
public class ConfigController extends BaseController {

    private final ConfigService configService;

    @Operation(summary = "获取系统配置表分页列表", description = "分页查询系统配置表信息")
    @Log(businessType = BusinessType.QUERY)
    @GetMapping("/page")
    public Result<Page<ConfigVO>> page(ConfigParam param) {
        return success(configService.selectConfigPage(param));
    }
    
    @Operation(summary = "获取系统配置表列表", description = "查询系统配置表列表信息")
    @Log(businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public Result<List<ConfigVO>> list(ConfigParam param) {
        return success(configService.selectConfigList(param));
    }

    @Operation(summary = "获取系统配置表详情", description = "根据ID查询系统配置表详细信息")
    @Parameter(name = "id", description = "系统配置表ID", required = true)
    @Log(businessType = BusinessType.QUERY)
    @GetMapping(value = "/{id}")
    public Result<ConfigVO> getInfo(@PathVariable("id") Long id) {
        return success(configService.getByIdRel(id));
    }

    @Operation(summary = "新增系统配置表", description = "新增系统配置表信息")
    @Log(businessType = BusinessType.INSERT)
    @PostMapping
    public Result<?> add(@Validated @RequestBody ConfigDTO dto) {
        configService.saveConfig(dto);
        return success();
    }

    @Operation(summary = "修改系统配置表", description = "修改系统配置表信息")
    @Log(businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<?> edit(@Validated @RequestBody ConfigDTO dto) {
        configService.updateConfig(dto);
        return success();
    }

    @Operation(summary = "删除系统配置表", description = "根据ID删除系统配置表信息")
    @Parameter(name = "id", description = "系统配置表ID", required = true)
    @Log(businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<?> remove(@PathVariable("id") Long id) {
        configService.deleteConfig(id);
        return success();
    }
}
