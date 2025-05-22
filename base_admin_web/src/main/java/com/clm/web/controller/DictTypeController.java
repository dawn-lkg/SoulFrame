package com.clm.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.domain.DictType;
import com.clm.system.service.DictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典类型 控制层
 *
 * @author 陈黎明
 * @date 2025/3/5
 */
@RestController
@RequestMapping("/system/dict/type")
@RequiredArgsConstructor
@Validated
public class DictTypeController extends BaseController {

    private final DictTypeService dictTypeService;

    /**
     * 获取字典类型列表
     */
    @SaCheckPermission("system:dict:list")
    @Log(value = "查询字典类型列表", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public Result<List<DictType>> list(DictType dictType) {
        List<DictType> list = dictTypeService.selectDictTypeList(dictType);
        return success(list);
    }

    /**
     * 获取字典类型详细信息
     */
    @SaCheckPermission("system:dict:query")
    @Log(value = "查询字典类型详情", businessType = BusinessType.QUERY)
    @GetMapping(value = "/{dictId}")
    public Result<DictType> getInfo(@PathVariable Long dictId) {
        return success(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    @SaCheckPermission("system:dict:add")
    @Log(value = "新增字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<?> add(@RequestBody DictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return success(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @SaCheckPermission("system:dict:edit")
    @Log(value = "修改字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<?> edit(@RequestBody DictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return success(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @SaCheckPermission("system:dict:remove")
    @Log(value = "删除字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public Result<?> remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }

    /**
     * 刷新字典缓存
     */
    @SaCheckPermission("system:dict:remove")
    @Log(value = "刷新字典缓存", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public Result<?> refreshCache() {
        dictTypeService.resetDictCache();
        return success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public Result<List<DictType>> optionselect() {
        List<DictType> dictTypes = dictTypeService.selectDictTypeAll();
        return success(dictTypes);
    }
} 