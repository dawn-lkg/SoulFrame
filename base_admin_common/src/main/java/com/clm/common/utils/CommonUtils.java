package com.clm.common.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈黎明
 * @date 2025/3/5 下午10:35
 */

@Component
@SuppressWarnings("all")
public class CommonUtils {
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String NUMBERS = "0123456789";
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTERS_NUMBERS = LETTERS + NUMBERS;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    /**
     * 生成指定长度的随机数字
     *
     * @param length 长度
     * @return 随机数字
     */
    public static String generateRandomNumbers(int length) {
        return generateRandom(length, NUMBERS);
    }

    /**
     * 生成指定长度的随机字母
     *
     * @param length 长度
     * @return 随机字母
     */
    public static String generateRandomLetters(int length) {
        return generateRandom(length, LETTERS);
    }

    /**
     * 生成指定长度的随机字母和数字
     *
     * @param length 长度
     * @return 随机字母和数字
     */
    public static String generateRandomString(int length) {
        return generateRandom(length, LETTERS_NUMBERS);
    }

    /**
     * 生成UUID（去除横线）
     *
     * @return UUID字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成验证码
     *
     * @param length 长度
     * @return 验证码
     */
    public static String generateCaptcha(int length) {
        return generateRandomNumbers(length);
    }

    /**
     * 生成随机字符串
     *
     * @param length 长度
     * @param source 源字符串
     * @return 随机字符串
     */
    private static String generateRandom(int length, String source) {
        if (length < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(source.charAt(SECURE_RANDOM.nextInt(source.length())));
        }
        return sb.toString();
    }

    /**
     * 从缓存中获取数据，如果不存在则通过回调函数获取并缓存
     *
     * @param key 缓存键
     * @param callback 回调函数，用于在缓存不存在时获取数据
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 缓存数据
     * @param <T> 数据类型
     */
    public <T> T getCache(String key, CacheCallback<T> callback, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        // 尝试从缓存获取
        Object value = ops.get(key);
        if (value != null) {
            return (T) value;
        }

        // 缓存不存在，通过回调获取数据
        T newValue = callback.get();
        if (newValue != null) {
            // 设置缓存
            ops.set(key, newValue, timeout, unit);
        }
        return newValue;
    }

    /**
     * 从缓存中获取数据，如果不存在则通过回调函数获取并缓存（默认过期时间24小时）
     *
     * @param key 缓存键
     * @param callback 回调函数，用于在缓存不存在时获取数据
     * @return 缓存数据
     * @param <T> 数据类型
     */
    public <T> T getCache(String key, CacheCallback<T> callback) {
        return getCache(key, callback, 24, TimeUnit.HOURS);
    }

    /**
     * 设置缓存
     *
     * @param key 缓存键
     * @param value 缓存值
     * @param timeout 超时时间
     * @param unit 时间单位
     */
    public void setCache(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 设置缓存（默认过期时间24小时）
     *
     * @param key 缓存键
     * @param value 缓存值
     */
    public void setCache(String key, Object value) {
        setCache(key, value, 24, TimeUnit.HOURS);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 判断缓存是否存在
     *
     * @param key 缓存键
     * @return 是否存在
     */
    public boolean hasCache(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 获取缓存过期时间
     *
     * @param key 缓存键
     * @return 过期时间（秒）
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 缓存回调接口
     *
     * @param <T> 数据类型
     */
    @FunctionalInterface
    public interface CacheCallback<T> {
        /**
         * 获取数据
         *
         * @return 数据
         */
        T get();
    }
}
