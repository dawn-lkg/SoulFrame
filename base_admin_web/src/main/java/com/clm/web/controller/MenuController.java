package com.clm.web.controller;

import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.utils.TreeUtils;
import com.clm.system.domain.dto.MenuDTO;
import com.clm.system.domain.param.MenuQueryParam;
import com.clm.system.domain.vo.MenuVO;
import com.clm.system.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单权限表(Menu)表控制层
 *
 * @author 陈黎明
 * @since 2025-03-08 10:56:36
 */
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
@Validated
public class MenuController extends BaseController {
    
    private final MenuService menuService;
    
    /**
     * 查询菜单列表
     * @param param 查询参数
     * @return 菜单列表
     */
    @GetMapping("/list")
    public Result<List<MenuVO>> list(MenuQueryParam param) {
        List<MenuVO> menus = menuService.selectMenuList(param);
        return success(menus);
    }
    
    /**
     * 获取菜单树
     * @param param 查询参数
     * @return 菜单树
     */
    @GetMapping("/tree")
    public Result<List<MenuVO>> tree(MenuQueryParam param) {
        List<MenuVO> menus = menuService.selectMenuList(param);
        return success(TreeUtils.buildTree(menus, MenuVO::getMenuId, MenuVO::getParentId, MenuVO::getChildren, menu->{menu.setChildren(null);return null;}));
    }
    
    /**
     * 获取菜单详情
     * @param menuId 菜单ID
     * @return 菜单详情
     */
    @GetMapping("/{menuId}")
    public Result<MenuVO> getInfo(@PathVariable Long menuId) {
        return success(menuService.getMenuInfo(menuId));
    }
    
    /**
     * 新增菜单
     * @param dto 菜单参数
     * @return 操作结果
     */
    @PostMapping
    public Result<?> add(@RequestBody @Valid MenuDTO dto) {
        menuService.addMenu(dto);
        return success();
    }
    
    /**
     * 修改菜单
     * @param dto 菜单参数
     * @return 操作结果
     */
    @PutMapping
    public Result<?> update(@RequestBody @Valid MenuDTO dto) {
        menuService.updateMenu(dto);
        return success();
    }
    
    /**
     * 删除菜单
     * @param menuId 菜单ID
     * @return 操作结果
     */
    @DeleteMapping("/{menuId}")
    public Result<?> delete(@PathVariable Long menuId) {
        if (menuService.deleteMenu(menuId)) {
            return success();
        }
        return error("删除菜单失败");
    }
    
    /**
     * 检查菜单名称是否唯一
     * @param dto 菜单参数
     * @return 结果
     */
    @GetMapping("/check-name-unique")
    public Result<Boolean> checkNameUnique(MenuDTO dto) {
        return success(menuService.checkMenuNameUnique(dto));
    }
}

