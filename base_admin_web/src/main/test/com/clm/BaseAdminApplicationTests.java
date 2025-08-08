package com.clm;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.clm.common.core.domain.entity.BeanMethod;
import com.clm.common.utils.CommonUtils;
import com.clm.common.utils.PasswordUtils;
import com.clm.framework.config.properties.TimerTaskPackProperties;
import com.clm.system.domain.param.VisitingStatisticParam;
import com.clm.system.domain.vo.VisitingRangeDataVO;
import com.clm.system.mapper.MenuMapper;
import com.clm.system.mapper.OperLogMapper;
import com.clm.system.service.MenuService;
import com.clm.system.service.RoleService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.*;

@SpringBootTest
class BaseAdminApplicationTests {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    // Object类的默认方法集合
    private static final Set<String> OBJECT_METHODS = new HashSet<>(Arrays.asList(
            "equals", "hashCode", "toString", "getClass", "notify", "notifyAll", "wait"
    ));
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private TimerTaskPackProperties timerTaskPackProperties;
    @Resource
    private OperLogMapper operLogMapper;

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

    @Test
    void test4() {
        Map<String, Object> beansOfType = applicationContext.getBeansOfType(Object.class);
        beansOfType.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
    }

    @Test
    void test5() {
        List<String> pack = timerTaskPackProperties.getPack();
        List<String> beanList = Arrays.asList(applicationContext.getBeanDefinitionNames());
        List<Map<String, List<BeanMethod>>> list = beanList.stream().filter(beanName -> {
            Object bean = applicationContext.getBean(beanName);
            String className = bean.getClass().getName();
            return pack.stream().anyMatch(className::startsWith);
        }).map(beanName -> {
            Map<String, List<BeanMethod>> map = new HashMap<>();
            Object bean = applicationContext.getBean(beanName);
            List<Method> methodList = Arrays.asList(bean.getClass().getMethods());
            List<BeanMethod> beanMethodList = methodList.stream().filter(method -> !OBJECT_METHODS.contains(method.getName())).map(method -> {
                BeanMethod beanMethod = new BeanMethod();
                String formatMethod = CommonUtils.formatMethodWithParams(method);
                String value = beanName + "." + formatMethod;
                beanMethod.setLabel(formatMethod);
                beanMethod.setValue(value);
                return beanMethod;
            }).toList();
            map.put(beanName, beanMethodList);
            return map;
        }).toList();
        System.out.println(list);
//        list.forEach(System.out::println);
//        for (String beanDefinitionName : beanDefinitionNames) {
//            Object bean = applicationContext.getBean(beanDefinitionName);
//            String
//            System.out.println(bean.getClass().getName());
////            System.out.println(beanDefinitionName);
////            for (String p : pack) {
////                if (beanDefinitionName.startsWith(p)) {
////                    System.out.println(beanDefinitionName);
////                }
////            }
//        }
    }

    @Test
    void test6() {
        String endTime = DateUtil.date().toString("yyyy-MM-dd HH");
        String startTime = DateUtil.date().offset(DateField.HOUR, -24).toString("yyyy-MM-dd HH");
        List<VisitingRangeDataVO> list = operLogMapper.getVisitingCountRange(new VisitingStatisticParam(startTime, endTime));
        System.out.println(list);
    }
}
