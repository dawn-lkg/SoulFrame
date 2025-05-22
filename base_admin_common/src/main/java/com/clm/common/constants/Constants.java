package com.clm.common.constants;

public interface Constants {
    
    /**
     * UTF-8 字符集
     */
    String UTF8 = "UTF-8";
    
    /**
     * 登录成功
     */
    String LOGIN_SUCCESS = "Success";
    
    /**
     * 注销
     */
    String LOGOUT = "Logout";
    
    /**
     * 注册
     */
    String REGISTER = "Register";
    
    /**
     * 登录失败
     */
    String LOGIN_FAIL = "Error";
    
    /**
     * 验证码 redis key
     */
    String CAPTCHA_CODE_KEY = "captcha_codes:";
    
    /**
     * 验证码有效期（分钟）
     */
    Integer CAPTCHA_EXPIRATION = 2;
} 