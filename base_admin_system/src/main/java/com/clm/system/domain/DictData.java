package com.clm.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clm.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据表 实体类
 *
 * @author 陈黎明
 * @date 2025/3/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
public class DictData extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    @TableId(value = "dict_code", type = IdType.AUTO)
    private Long dictCode;

    /**
     * 字典排序
     */
    @TableField("dict_sort")
    private Integer dictSort;

    /**
     * 字典标签
     */
    @TableField("dict_label")
    private String dictLabel;

    /**
     * 字典键值
     */
    @TableField("dict_value")
    private String dictValue;

    /**
     * 字典类型
     */
    @TableField("dict_type")
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    @TableField("css_class")
    private String cssClass;

    /**
     * 表格回显样式
     */
    @TableField("list_class")
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    @TableField("is_default")
    private String isDefault;

    /**
     * 状态（0正常 1停用）
     */
    @TableField("status")
    private String status;
} 