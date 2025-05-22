package com.clm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clm.common.constants.RedisKeyConstants;
import com.clm.common.utils.RedisUtils;
import com.clm.system.domain.DictData;
import com.clm.system.mapper.DictDataMapper;
import com.clm.system.service.DictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 字典数据表 服务实现
 *
 * @author 陈黎明
 * @date 2025/3/5
 */
@Service
@RequiredArgsConstructor
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    private final DictDataMapper dictDataMapper;
    
    private final RedisUtils redisUtils;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合
     */
    @Override
    public List<DictData> selectDictDataList(DictData dictData) {
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(dictData)) {
            if (StringUtils.hasText(dictData.getDictType())) {
                queryWrapper.eq(DictData::getDictType, dictData.getDictType());
            }
            if (StringUtils.hasText(dictData.getDictLabel())) {
                queryWrapper.like(DictData::getDictLabel, dictData.getDictLabel());
            }
            if (StringUtils.hasText(dictData.getStatus())) {
                queryWrapper.eq(DictData::getStatus, dictData.getStatus());
            }
        }
        queryWrapper.orderByAsc(DictData::getDictSort);
        return dictDataMapper.selectList(queryWrapper);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        if (StringUtils.hasText(dictType) && StringUtils.hasText(dictValue)) {
            // 尝试从缓存获取
            String dictKey = RedisKeyConstants.System.DICT_PREFIX + dictType;
            List<DictData> dictDataList = redisUtils.get(dictKey, List.class);
            
            // 如果缓存中没有，则从数据库查询
            if (dictDataList == null || dictDataList.isEmpty()) {
                dictDataList = selectDictDataByType(dictType);
                // 将查询结果放入缓存
                if (dictDataList != null && !dictDataList.isEmpty()) {
                    redisUtils.set(dictKey, dictDataList);
                }
            }
            
            // 遍历查找匹配的字典值
            if (dictDataList != null && !dictDataList.isEmpty()) {
                for (DictData data : dictDataList) {
                    if (dictValue.equals(data.getDictValue())) {
                        return data.getDictLabel();
                    }
                }
            }
        }
        return "";
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public DictData selectDictDataById(Long dictCode) {
        return dictDataMapper.selectById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        // 获取删除字典数据的类型，用于删除缓存
        Arrays.stream(dictCodes).forEach(dictCode -> {
            DictData dictData = dictDataMapper.selectById(dictCode);
            if (Objects.nonNull(dictData)) {
                String dictType = dictData.getDictType();
                // 删除缓存
                redisUtils.delete(RedisKeyConstants.System.DICT_PREFIX + dictType);
            }
        });
        
        // 批量删除字典数据
        for (Long dictCode : dictCodes) {
            dictDataMapper.deleteById(dictCode);
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public boolean insertDictData(DictData dictData) {
        // 删除缓存
        redisUtils.delete(RedisKeyConstants.System.DICT_PREFIX + dictData.getDictType());
        return dictDataMapper.insert(dictData) > 0;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public boolean updateDictData(DictData dictData) {
        // 获取旧的字典数据
        DictData oldDict = dictDataMapper.selectById(dictData.getDictCode());
        if (Objects.nonNull(oldDict)) {
            // 如果字典类型发生变化，则删除旧类型的缓存
            if (!oldDict.getDictType().equals(dictData.getDictType())) {
                redisUtils.delete(RedisKeyConstants.System.DICT_PREFIX + oldDict.getDictType());
            }
        }
        // 删除当前字典类型的缓存
        redisUtils.delete(RedisKeyConstants.System.DICT_PREFIX + dictData.getDictType());
        return dictDataMapper.updateById(dictData) > 0;
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合
     */
    @Override
    public List<DictData> selectDictDataByType(String dictType) {
        // 尝试从缓存获取
        String dictKey = RedisKeyConstants.System.DICT_PREFIX + dictType;
        List<DictData> dictDataList = redisUtils.get(dictKey, List.class);
        
        // 如果缓存中没有，则从数据库查询
        if (dictDataList == null || dictDataList.isEmpty()) {
            dictDataList = dictDataMapper.selectDictDataByType(dictType);
            // 将查询结果放入缓存
            if (dictDataList != null && !dictDataList.isEmpty()) {
                redisUtils.set(dictKey, dictDataList);
            }
        }
        
        return dictDataList;
    }
} 