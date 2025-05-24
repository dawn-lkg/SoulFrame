package com.clm;

import cn.dev33.satoken.secure.BCrypt;
import com.clm.common.utils.PasswordUtils;
import com.clm.system.mapper.MenuMapper;
import com.clm.system.service.MenuService;
import com.clm.system.service.RoleService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BaseAdminApplicationTests {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    @Test
    void contextLoads() {
        String pw_hash = BCrypt.hashpw("123456", BCrypt.gensalt());
        System.out.println(BCrypt.checkpw("1234567", pw_hash));
        System.out.println(pw_hash);
    }

    @Test
    void test() {
        System.out.println(menuMapper.selectMenuVoByUserId(1L));
    }

    @Test
    void test2() {

    }

    @Test
    void test3() {
        System.out.println(PasswordUtils.encryptPassword("123456"));
        System.out.println(PasswordUtils.matches("123456", PasswordUtils.encryptPassword("123456")));
    }
}
