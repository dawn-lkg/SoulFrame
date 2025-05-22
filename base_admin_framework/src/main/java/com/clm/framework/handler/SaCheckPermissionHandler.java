package com.clm.framework.handler;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.annotation.handler.SaAnnotationHandlerInterface;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author 陈黎明
 * @date 2025/5/5 下午3:38
 */
@Slf4j
@Component
public class SaCheckPermissionHandler implements SaAnnotationHandlerInterface<SaCheckPermission> {

    @Override
    public Class<SaCheckPermission> getHandlerAnnotationClass() {
        return SaCheckPermission.class;
    }

    @Override
    public void checkMethod(SaCheckPermission sa, Method method) {
        _checkMethod(sa.type(), sa.value(), sa.mode(), sa.orRole());
    }

    public static void _checkMethod(String type, String[] value, SaMode mode, String[] orRole) {
        StpLogic stpLogic = SaManager.getStpLogic(type, false);

        String[] permissionArray = value;
        try {
            if (mode == SaMode.AND) {
                stpLogic.checkPermissionAnd(permissionArray);
            } else {
                stpLogic.checkPermissionOr(permissionArray);
            }
        } catch (NotPermissionException e) {
//            // Start------------以下是自定义代码-------
//            if (LoginUtils.isSuperAdmin())
//                return;
//            // End------------以上是自定义代码-------
//            // 权限认证校验未通过，再开始角色认证校验
//            for (String role : orRole) {
//                String[] rArr = SaFoxUtil.convertStringToArray(role);
//                // 某一项 role 认证通过，则可以提前退出了，代表通过
//                if (stpLogic.hasRoleAnd(rArr)) {
//                    return;
//                }
//            }
            throw e;
        }
    }
}
