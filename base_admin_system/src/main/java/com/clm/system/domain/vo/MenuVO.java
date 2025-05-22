package com.clm.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单视图对象
 *
 * @author 陈黎明
 * @since 2025-03-08
 */
@Data
public class MenuVO {
    
    /**
     * 菜单ID
     */
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
     * 父菜单名称
     */
    private String parentName;
    
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
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 子菜单
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuVO> children = new ArrayList<>();
}
