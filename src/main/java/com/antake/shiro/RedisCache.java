package com.antake.shiro;

import com.alibaba.fastjson.JSON;
import com.antake.components.RedisUtil;
import com.antake.constants.Constant;
import com.antake.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Antake
 * @date 2020/5/8
 * @description this is description
 */
@Slf4j
public class RedisCache<K,V> implements Cache<K,V> {
    private long expire=24;
    private RedisUtil redisUtil;
    private static String cacheKey= Constant.IDENTIFY_CACHE_KEY;
    public RedisCache(RedisUtil redisUtil) {
        this.redisUtil=redisUtil;
    }

    @Override
    public V get(K k) throws CacheException {
        log.info("Shiro从缓存中获取数据key值[{}]",k);
        if (k==null){
            return null;
        }
        try {
            String cacheKey = getCacheKey(k);
            Object value = redisUtil.get(cacheKey);
            System.out.println(value);
            if (value==null){
                return null;
            }
            //SimpleAuthorizationInfo simpleAuthorizationInfo= JSON.parseObject(String.valueOf(value),SimpleAuthorizationInfo.class);
            return (V)value;
        }catch (Exception e){
            throw new CacheException(e);
        }
    }

    @Override
    public V put(K k, V v) throws CacheException {
        log.info("put key[{}]",k);
        if (k==null){
            log.warn("saving a null key is meaningless,return value directly without call redis");
            return v;
        }
        try {
            String cacheKey = getCacheKey(k);
            redisUtil.set(cacheKey,v!=null?v:null,expire, TimeUnit.HOURS);
            return v;
        }catch (Exception e){
            throw new CacheException(e);
        }
    }

    @Override
    public V remove(K k) throws CacheException {
        log.info("remove key[{}]",k);
        if (k==null){
            return null;
        }
        try {
            String cacheKey = getCacheKey(k);
            Object o = redisUtil.get(cacheKey);
            V value=(V)o;
            redisUtil.delete(cacheKey);
            return value;
        }catch (Exception e){
            throw new CacheException(e);
        }
    }

    @Override
    public void clear() throws CacheException {
        log.debug("clear cache");
        Set<String> keys=null;
        try {
            keys=redisUtil.keys(cacheKey+"*");
        }catch (Exception e){
            log.error("get keys error",e);
        }
        if (keys==null || keys.size()==0){
            return;
        }
        redisUtil.delete(keys);
    }

    @Override
    public int size() {
        int result=0;
        try {
            result=redisUtil.keys(cacheKey+"*").size();
        }catch (Exception e){
            log.error("get keys error",e);
        }
        return result;
    }

    @Override
    public Set<K> keys() {
        Set<String> keys=null;
        try {
            keys=redisUtil.keys(cacheKey+"*");
        }catch (Exception e){
            log.error("get keys error",e);
            return Collections.emptySet();
        }
        if (CollectionUtils.isEmpty(keys)){
            return Collections.emptySet();
        }
        try {
            return keys.stream().map(str->(K)str).collect(Collectors.toSet());
        }catch (Exception e){
            log.error("deserialize keys error",e);
        }
        return Collections.emptySet();
    }

    @Override
    public Collection<V> values() {
        Set<String> keys=null;
        try {
            keys=redisUtil.keys(cacheKey+"*");
        }catch (Exception e){
            log.error("get keys error",e);
            return Collections.emptySet();
        }
        if (CollectionUtils.isEmpty(keys)){
            return Collections.emptySet();
        }
        try {
            return Collections.unmodifiableList(keys.stream().filter(s -> s!=null).map(str->(V)str).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("deserialize values error",e);
        }
        return Collections.emptyList();
    }

    private String getCacheKey(K token){
        if (token==null){
            return null;
        }else {
            return cacheKey.concat(JwtUtil.getUserId(token.toString()));
        }
    }
}

