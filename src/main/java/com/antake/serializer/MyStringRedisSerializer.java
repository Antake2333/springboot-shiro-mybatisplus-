package com.antake.serializer;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Antake
 * @date 2020/5/7
 * @description this is description
 */
public class MyStringRedisSerializer implements RedisSerializer<Object> {
    private final Charset charset;

    public static final MyStringRedisSerializer US_ASCII = new MyStringRedisSerializer(StandardCharsets.US_ASCII);

    public static final MyStringRedisSerializer ISO_8859_1 = new MyStringRedisSerializer(StandardCharsets.ISO_8859_1);

    public static final MyStringRedisSerializer UTF_8 = new MyStringRedisSerializer(StandardCharsets.UTF_8);

    public MyStringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public MyStringRedisSerializer(Charset charset) {

        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    @Override
    public byte[] serialize(@Nullable Object object) {
        if (object==null){
            return new byte[0];
        }
        if (object instanceof String){
            return object.toString().getBytes(charset);
        }else {
            return JSON.toJSONString(object).getBytes();
        }
    }
}

