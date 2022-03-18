package com.antake.shiro;

import com.antake.components.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Antake
 * @date 2020/5/8
 * @description this is description
 */
public class ShiroCacheManager implements CacheManager {
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new RedisCache<>(redisUtil);
    }
}

