package com.clm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clm.system.domain.DictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典数据表 数据层
 *
 * @author 陈黎明
 * @date 2025/3/5
 */
public interface DictDataMapper extends BaseMapper<DictData> {
    
    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合
     */
    List<DictData> selectDictDataByType(@Param("dictType") String dictType);
} 