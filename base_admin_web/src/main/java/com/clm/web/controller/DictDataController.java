package com.clm.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.domain.DictData;
import com.clm.system.service.DictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典数据 控制层
 *
 * @author 陈黎明
 * @date 2025/3/5
 */
@RestController
@RequestMapping("/system/dict/data")
@RequiredArgsConstructor
@Validated
public class DictDataController extends BaseController {

    private final DictDataService dictDataService;

    /**
     * 查询字典数据列表
     */
    @SaCheckPermission("system:dict:list")
    @Log(value = "查询字典数据列表", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public Result<List<DictData>> list(DictData dictData) {
        List<DictData> list = dictDataService.selectDictDataList(dictData);
        return success(list);
    }

    /**
     * 查询字典数据详细
     */
    @SaCheckPermission("system:dict:query")
    @Log(value = "查询字典数据详情", businessType = BusinessType.QUERY)
    @GetMapping(value = "/{dictCode}")
    public Result<DictData> getInfo(@PathVariable Long dictCode) {
        return success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public Result<List<DictData>> dictType(@PathVariable String dictType) {
        List<DictData> data = dictDataService.selectDictDataByType(dictType);
        return success(data);
    }

    /**
     * 新增字典数据
     */
    @SaCheckPermission("system:dict:add")
    @Log(value = "新增字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<?> add(@RequestBody DictData dict) {
        return success(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典数据
     */
    @SaCheckPermission("system:dict:edit")
    @Log(value = "修改字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<?> edit(@RequestBody DictData dict) {
        return success(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典数据
     */
    @SaCheckPermission("system:dict:remove")
    @Log(value = "删除字典数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public Result<?> remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }
} 