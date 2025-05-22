package com.clm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clm.common.constants.RedisKeyConstants;
import com.clm.common.exception.BaseException;
import com.clm.common.utils.RedisUtils;
import com.clm.system.domain.DictData;
import com.clm.system.domain.DictType;
import com.clm.system.mapper.DictDataMapper;
import com.clm.system.mapper.DictTypeMapper;
import com.clm.system.service.DictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 字典类型表 服务实现
 *
 * @author 陈黎明
 * @date 2025/3/5
 */
@Service
@RequiredArgsConstructor
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    private final DictTypeMapper dictTypeMapper;
    
    private final DictDataMapper dictDataMapper;
    
    private final RedisUtils redisUtils;

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合
     */
    @Override
    public List<DictType> selectDictTypeList(DictType dictType) {
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
        if (dictType != null) {
            if (StringUtils.hasText(dictType.getDictName())) {
                queryWrapper.like(DictType::getDictName, dictType.getDictName());
            }
            if (StringUtils.hasText(dictType.getDictType())) {
                queryWrapper.like(DictType::getDictType, dictType.getDictType());
            }
            if (StringUtils.hasText(dictType.getStatus())) {
                queryWrapper.eq(DictType::getStatus, dictType.getStatus());
            }
        }
        return dictTypeMapper.selectList(queryWrapper);
    }

    /**
     * 查询所有字典类型
     *
     * @return 字典类型集合
     */
    @Override
    public List<DictType> selectDictTypeAll() {
        return dictTypeMapper.selectList(null);
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public DictType selectDictTypeById(Long dictId) {
        return dictTypeMapper.selectById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public DictType selectDictTypeByType(String dictType) {
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictType::getDictType, dictType);
        return dictTypeMapper.selectOne(queryWrapper);
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            DictType dictType = dictTypeMapper.selectById(dictId);
            if (Objects.nonNull(dictType)) {
                // 删除字典类型
                dictTypeMapper.deleteById(dictId);
                // 删除相关字典数据
                LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(DictData::getDictType, dictType.getDictType());
                dictDataMapper.delete(queryWrapper);
                // 删除缓存
                String dictKey = RedisKeyConstants.System.DICT_PREFIX + dictType.getDictType();
                redisUtils.delete(dictKey);
            }
        }
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        // 删除所有字典缓存
        redisUtils.deleteByPattern(RedisKeyConstants.System.DICT_PREFIX + "*");
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    public boolean insertDictType(DictType dictType) {
        // 检查是否唯一
        if (!checkDictTypeUnique(dictType)) {
            throw new BaseException("新增字典'" + dictType.getDictName() + "'失败，字典类型已存在");
        }
        return dictTypeMapper.insert(dictType) > 0;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDictType(DictType dictType) {
        // 检查是否唯一
        if (!checkDictTypeUnique(dictType)) {
            throw new BaseException("修改字典'" + dictType.getDictName() + "'失败，字典类型已存在");
        }
        
        DictType oldDict = dictTypeMapper.selectById(dictType.getDictId());
        if (Objects.nonNull(oldDict)) {
            // 如果字典类型发生变化，则更新字典数据表的字典类型
            if (!oldDict.getDictType().equals(dictType.getDictType())) {
                // 更新字典数据表中的字典类型
                DictData dictData = new DictData();
                dictData.setDictType(dictType.getDictType());
                
                LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(DictData::getDictType, oldDict.getDictType());
                
                dictDataMapper.update(dictData, queryWrapper);
                
                // 删除旧缓存
                String oldDictKey = RedisKeyConstants.System.DICT_PREFIX + oldDict.getDictType();
                redisUtils.delete(oldDictKey);
            }
        }
        
        return dictTypeMapper.updateById(dictType) > 0;
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    @Override
    public boolean checkDictTypeUnique(DictType dictType) {
        Long dictId = Objects.isNull(dictType.getDictId()) ? -1L : dictType.getDictId();
        
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictType::getDictType, dictType.getDictType());
        
        DictType info = dictTypeMapper.selectOne(queryWrapper);
        return info == null || info.getDictId().equals(dictId);
    }
} 