package com.clm.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.core.domain.entity.Role;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.domain.dto.RoleDTO;
import com.clm.system.domain.param.RoleQueryParam;
import com.clm.system.domain.vo.RoleVO;
import com.clm.system.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息表(Role)表控制层
 *
 * @author 陈黎明
 * @since 2025-03-08 10:57:22
 */
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
@Validated
public class RoleController extends BaseController {
    
    private final RoleService roleService;
    
    /**
     * 分页查询角色列表
     * @param param 查询参数
     * @return 分页结果
     */
//    @SaCheckPermission("system:role:query")
    @Log(value = "分页查询角色列表",businessType = BusinessType.QUERY)
    @GetMapping("/page")
    public Result<IPage<RoleVO>> page(RoleQueryParam param) {
        return success(roleService.pageRoleList(param));
    }


    /**
     * 获取所有角色列表
     * @return 角色列表
     */
    @Log(value = "获取所有角色列表",businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public Result<List<Role>> list() {
        return success(roleService.list());
    }

    /**
     * 获取角色详情
     * @param roleId 角色ID
     * @return 角色详情
     */
    @Log(value = "获取角色详情",businessType = BusinessType.QUERY)
    @GetMapping("/{roleId}")
    public Result<RoleVO> getInfo(@PathVariable Long roleId) {
        return success(roleService.getRoleInfo(roleId));
    }
    
    /**
     * 新增角色
     * @param param 角色参数
     * @return 操作结果
     */
    @Log(value = "新增角色",businessType = BusinessType.INSERT)
    @PostMapping
    public Result<?> add(@RequestBody @Valid RoleDTO param) {
        roleService.addRole(param);
        return success();
    }
    
    /**
     * 修改角色
     * @param param 角色参数
     * @return 操作结果
     */
    @Log(value = "修改角色",businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<?> update(@RequestBody @Valid RoleDTO param) {
        roleService.updateRole(param);
        return success();
    }
    
    /**
     * 删除角色
     * @param roleId 角色ID
     * @return 操作结果
     */
    @Log(value = "删除角色",businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleId}")
    public Result<?> delete(@PathVariable Long roleId) {
        if (roleService.deleteRole(roleId)) {
            return success();
        }
        return error("删除角色失败");
    }
    
    /**
     * 批量删除角色
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    @Log(value = "批量删除角色",businessType = BusinessType.DELETE)
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@RequestBody List<Long> roleIds) {
        if (roleService.batchDeleteRole(roleIds)) {
            return success();
        }
        return error("批量删除角色失败");
    }
    
    /**
     * 修改角色状态
     * @param roleId 角色ID
     * @param status 状态
     * @return 操作结果
     */
    @Log("修改角色状态")
    @PutMapping("/{roleId}/status/{status}")
    public Result<?> changeStatus(@PathVariable Long roleId, @PathVariable String status) {
        if (roleService.changeRoleStatus(roleId, status)) {
            return success();
        }
        return error("修改角色状态失败");
    }
    
    /**
     * 校验角色名称是否唯一
     * @param roleName 角色名称
     * @param roleId 角色ID
     * @return 校验结果
     */
    @Log("校验角色名称是否唯一")
    @GetMapping("/check-name-unique")
    public Result<Boolean> checkNameUnique(String roleName, Long roleId) {
        return success(roleService.checkRoleNameUnique(roleName, roleId));
    }
    
    /**
     * 校验角色权限是否唯一
     * @param roleKey 角色权限
     * @param roleId 角色ID
     * @return 校验结果
     */
    @Log("校验角色权限是否唯一")
    @GetMapping("/check-key-unique")
    public Result<Boolean> checkKeyUnique(String roleKey, Long roleId) {
        return success(roleService.checkRoleKeyUnique(roleKey, roleId));
    }

    /**
     * 分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    @Log("分配权限")
    @PutMapping("/{roleId}/permission")
    public Result<?> assignPermission(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        roleService.assignPermission(roleId, permissionIds);
        return success();
    }

    /**
     * 根据角色id查询菜单
     * @param roleId 角色ID
     * @return 菜单id列表
     */
    @Log("根据角色id查询菜单id列表")
    @GetMapping("/{roleId}/menu")
    public Result<List<Long>> getRoleMenu(@PathVariable Long roleId) {
        return success(roleService.getPermissionIdsByRoleId(roleId));
    }
}

