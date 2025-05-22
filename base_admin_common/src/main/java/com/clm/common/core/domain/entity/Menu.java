package com.clm.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.clm.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
/**
 * 菜单权限表(Menu)实体类
 *
 * @author 陈黎明
 * @since 2025-03-08 10:56:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class Menu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 227427138504536795L;
            
    /**
    * 菜单ID
    */
    @TableId
    private Long menuId;
            
    /**
    * 菜单名称
    */
    private String menuName;
            
    /**
    * 父菜单ID
    */
    private Long parentId;
            
    /**
    * 显示顺序
    */
    private Integer orderNum;
            
    /**
    * 路由地址
    */
    private String path;
            
    /**
    * 组件路径
    */
    private String component;
            
    /**
    * 路由参数
    */
    private String query;
            
    /**
    * 是否为外链（0是 1否）
    */
    private Integer isFrame;
            
    /**
    * 是否缓存（0缓存 1不缓存）
    */
    private Integer isCache;
            
    /**
    * 菜单类型（M目录 C菜单 F按钮）
    */
    private String menuType;
            
    /**
    * 菜单状态（0显示 1隐藏）
    */
    private String visible;
            
    /**
    * 菜单状态（0正常 1停用）
    */
    private String status;
            
    /**
    * 权限标识
    */
    private String perms;
            
    /**
    * 菜单图标
    */
    private String icon;
}

