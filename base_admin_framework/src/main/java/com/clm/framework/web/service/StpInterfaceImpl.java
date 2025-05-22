package com.clm.framework.web.service;

import cn.dev33.satoken.stp.StpInterface;
import com.clm.common.constants.RedisKeyConstants;
import com.clm.common.utils.RedisUtils;
import com.clm.system.service.MenuService;
import com.clm.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * sa-token 权限管理实现
 * 
 * @author 陈黎明
 * @date 2025/5/5 下午2:09
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    /**
     * 权限缓存时间（分钟）
     */
    private static final long CACHE_TIMEOUT = 30L;
    
    private final RoleService roleService;
    private final MenuService menuService;
    private final RedisUtils redisUtils;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String userId = String.valueOf(loginId);
        String key = RedisKeyConstants.getFullKey(RedisKeyConstants.Auth.USER_PERMS_PREFIX, userId);
        
        return redisUtils.get(key, () -> {
            Long uid = Long.valueOf(userId);
            log.debug("从数据库获取用户权限 userId: {}", uid);
            return menuService.getPermissionsByUserId(uid);
        }, CACHE_TIMEOUT, TimeUnit.MINUTES);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String userId = String.valueOf(loginId);
        String key = RedisKeyConstants.getFullKey(RedisKeyConstants.Auth.USER_ROLES_PREFIX, userId);
        
        return redisUtils.get(key, () -> {
            Long uid = Long.valueOf(userId);
            log.debug("从数据库获取用户角色 userId: {}", uid);
            return roleService.getRoleKeysByUserId(uid);
        }, CACHE_TIMEOUT, TimeUnit.MINUTES);
    }
    
    /**
     * 清除用户权限缓存
     * 
     * @param userId 用户ID
     */
    public void clearUserPermissionCache(Long userId) {
        String key = RedisKeyConstants.getFullKey(RedisKeyConstants.Auth.USER_PERMS_PREFIX, String.valueOf(userId));
        redisUtils.delete(key);
        log.debug("清除用户权限缓存 userId: {}", userId);
    }
    
    /**
     * 清除用户角色缓存
     * 
     * @param userId 用户ID
     */
    public void clearUserRoleCache(Long userId) {
        String key = RedisKeyConstants.getFullKey(RedisKeyConstants.Auth.USER_ROLES_PREFIX, String.valueOf(userId));
        redisUtils.delete(key);
        log.debug("清除用户角色缓存 userId: {}", userId);
    }
    
    /**
     * 清除角色相关所有用户的权限缓存
     *
     * @param roleId 角色ID
     */
    public void clearRoleUsersPermissionCache(Long roleId) {
        List<Long> userIds = roleService.getUserIdsByRoleId(roleId);
        if (userIds != null && !userIds.isEmpty()) {
            userIds.forEach(this::clearUserPermissionCache);
            userIds.forEach(this::clearUserRoleCache);
            log.info("清除角色[{}]下所有用户的权限缓存，共{}个用户", roleId, userIds.size());
        }
    }
    
    /**
     * 清除菜单相关所有角色用户的权限缓存
     * 
     * @param menuId 菜单ID
     */
    public void clearMenuRelatedPermissionCache(Long menuId) {
        List<Long> roleIds = menuService.getRoleIdsByMenuId(menuId);
        if (roleIds != null && !roleIds.isEmpty()) {
            roleIds.forEach(this::clearRoleUsersPermissionCache);
            log.info("清除菜单[{}]相关的所有角色用户权限缓存，共{}个角色", menuId, roleIds.size());
        }
    }
}
