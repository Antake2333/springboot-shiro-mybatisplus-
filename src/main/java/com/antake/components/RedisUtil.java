package com.antake.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Antake
 * @date 2020/4/28
 * @description this is description
 */
@Component
public class RedisUtil {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    /**-----------------------------key相关操作-------------------------------------*/
    /**
     * @Description: 是否存在key
     * @Param: [key]
     * @return: boolean
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:10
     */
    public boolean hasKey(String key) {
        return key == null ? false : redisTemplate.hasKey(key);
    }

    /**
     * @Description: 删除key
     * @Param: [key]
     * @return: boolean
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:10
     */
    public boolean delete(String key) {
        return key == null ? false : redisTemplate.delete(key);
    }

    /**
     * @Description: 批量删除key
     * @Param: [keys]
     * @return: java.lang.Long
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:12
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * @Description: 设置过期时间
     * @Param: [key, timeout, unit]
     * @return: java.lang.Boolean
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:15
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        if (key == null || unit == null) {
            return false;
        }
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * @Description: 查找匹配的key
     * @Param: [pattern]
     * @return: java.util.Set<java.lang.String>
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:16
     */
    public Set<String> keys(String pattern) {
        if (pattern == null) {
            return null;
        }
        return redisTemplate.keys(pattern);
    }

    /**
     * @Description: 移除key的过期时间，key将持久保持
     * @Param: [key]
     * @return: java.lang.Boolean
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:18
     */
    public Boolean persist(String key) {
        if (key == null) {
            return false;
        }
        return redisTemplate.persist(key);
    }

    /**
     * @Description: 查看key的过期时间, key或者unit不存在时返回0，key存在但是没有设置生存时间，返回-1，key不存在返回-2
     * @Param: [key, unit]
     * @return: java.lang.Long
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:20
     */
    public Long getExpire(String key, TimeUnit unit) {
        if (key == null || unit == null) {
            throw new IllegalArgumentException("getExpire 参数不能为null");
        }
        return redisTemplate.getExpire(key, unit);
    }
    /**-----------------------------String相关操作-------------------------------------*/
    /**
     * @Description: 设置key-value
     * @Param: [key, value]
     * @return: void
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:23
     */
    public void set(String key, Object value) {
        if (key == null || value == null) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @Description: 设置key value并设置过期时间
     * @Param: [key, value, Time, unit]
     * @return: void
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:26
     */
    public void set(String key, Object value, long Time, TimeUnit unit) {
        if (key == null || value == null || unit == null) {
            return;
        }
        redisTemplate.opsForValue().set(key, value, Time, unit);
    }

    /**
     * @Description: 设置key-value并设置过期时间，有返回值
     * @Param: [key, value, Time, unit]
     * @return: java.lang.Boolean
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:31
     */
    public Boolean setIfAbsent(String key, Object value, long Time, TimeUnit unit) {
        if (key == null || value == null || unit == null) {
            throw new IllegalArgumentException("setIfAbsent 参数不能为null");
        }
        return redisTemplate.opsForValue().setIfAbsent(key, value, Time, unit);
    }

    /**
     * @Description: 获取指定key的值
     * @Param: [key]
     * @return: java.lang.Object
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:34
     */
    public Object get(String key) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @Description: 先获取，再设置
     * @Param: [key, value]
     * @return: java.lang.Object
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:36
     */
    public Object getSet(String key, Object value) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * @Description: 批量key获取批量value
     * @Param: [keys]
     * @return: java.util.List<java.lang.Object>
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:38
     */
    public List<Object> mget(Collection<String> keys) {
        if (keys == null) {
            return Collections.emptyList();
        }
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * @Description: 将指定Key的Value原子性的增加increment。如果该Key不存在，其初始值为0，
     * 在incrby之后其值为increment。如果Value的值不能转换为整型值，如Hi，
     * 该操作将执行失败并抛出相应异常。操作成功则返回增加后的value值
     * @Param: [key, increment]
     * @return: long
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:43
     */
    public Long incrby(String key, long increment) {
        if (key == null) {
            throw new IllegalArgumentException("incrby 参数 key 不能为null");
        }
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * @Description: 将指定key的value原子性的减少decrement, 如果key不存在，其初始冷0，
     * 在decrby之后其值decrement,如果value的值不能转换为整数值，如果执行失败就抛出异常，操作成功后返回减少后的值
     * @Param: [key, decrement]
     * @return: java.lang.Long
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:46
     */
    public Long decrby(String key, long decrement) {
        if (key == null) {
            throw new IllegalArgumentException("decrby 参数 key 不能为null");
        }
        return redisTemplate.opsForValue().decrement(key, decrement);
    }

    /**
     * @Description: 如果key存在，append命令将参数value的数据追加到已存在value的末尾，
     * 如果key不存在，append命令就会创建一个新的 key/value，返回追加后的value的字符串长度
     * @Param: [key, value]
     * @return: java.lang.Integer
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:50
     */
    public Integer append(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("append 参数 key 不能为null");
        }
        return redisTemplate.opsForValue().append(key, value);
    }
    /**-----------------------------hash相关操作-------------------------------------*/
    /**
     * @Description: 通过key和field获取指定的value
     * @Param: [key, field]
     * @return: java.lang.Object
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:52
     */
    public Object hGet(String key, Object field) {
        if (key == null || field == null) {
            return null;
        }
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * @Description: 设置key field value
     * @Param: [key, field, value]
     * @return: void
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:54
     */
    public void hSet(String key, Object field, Object value) {
        if (key == null || field == null) {
            return;
        }
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * @Description: 判断指定的key中是否存在指定的field
     * @Param: [key, field]
     * @return: java.lang.Boolean
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:55
     */
    public Boolean hExists(String key, Object field) {
        if (key == null || field == null) {
            return false;
        }
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * @Description: 删除指定key中的多个字段
     * @Param: [key, fields]
     * @return: java.lang.Long
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:57
     */
    public Long hDel(String key, Object... fields) {
        if (key == null || fields == null || fields.length == 0) {
            return 0L;
        }
        return redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * @Description: 通过指定的key获取所有的field和value
     * @Param: [key]
     * @return: java.util.Map<java.lang.Object, java.lang.Object>
     * @Author: MiZiMi
     * @Date: 2020/4/28 17:59
     */
    public Map<Object, Object> hgetAll(String key) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @Description: 批量设置hash的field/value
     * @Param: [key, hash]
     * @return: void
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:01
     */
    public void hmSet(String key, Map<String, Object> hash) {
        if (key == null || hash == null) {
            return;
        }
        redisTemplate.opsForHash().putAll(key, hash);
    }

    /**
     * @Description: 获取指定key中的一组filed的一组value的值
     * @Param: [key, fields]
     * @return: java.util.List<java.lang.Object>
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:04
     */
    public List<Object> hmGet(String key, Collection<Object> fields) {
        if (key == null || fields == null) {
            return null;
        }
        return redisTemplate.opsForHash().multiGet(key, fields);
    }

    /**
     * @Description: 对应key的字段自增相应的值
     * @Param: [key, field, increment]
     * @return: java.lang.Long
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:06
     */
    public Long hIncrby(String key, Object field, long increment) {
        if (key == null || field == null) {
            throw new IllegalArgumentException("hIncrby key or field 不能为空");
        }
        return redisTemplate.opsForHash().increment(key, field, increment);
    }
    /**-----------------------------List相关操作-------------------------------------*/
    /**
     * @Description: 向列表左边添加元素。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，
     * 之后再将数据从链表的头部插入。
     * 如果该键的Value不是链表类型，该命令将将会抛出相关异常。操作成功则返回插入后链表中元素的数量
     * @Param: [key, strs]
     * @return: java.lang.Long
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:09
     */
    public Long lPush(String key, Object... strs) {
        if (key == null) {
            return 0L;
        }
        return redisTemplate.opsForList().leftPushAll(key, strs);
    }

    /**
     * @Description: 向列表右边添加元素。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，
     * 之后再将数据从链表的头部插入。
     * 如果该键的Value不是链表类型，该命令将将会抛出相关异常。操作成功则返回插入后链表中元素的数量
     * @Param: [key, strs]
     * @return: java.lang.Long
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:11
     */
    public Long rPush(String key, Object... strs) {
        if (key == null) {
            return 0L;
        }
        return redisTemplate.opsForList().rightPushAll(key, strs);
    }

    /**
     * @Description: 从左边弹出指定key的value
     * @Param: [key]
     * @return: java.lang.Object
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:12
     */
    public Object lPop(String key) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * @Description: 从右边弹出指定key的value
     * @Param: [key]
     * @return: java.lang.Object
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:12
     */
    public Object rPop(String key) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * @Description:取返回
     * @Param: [key, start, end]
     * @return: java.util.List<java.lang.Object>
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:16
     */
    public List<Object> lRange(String key, long start, long end) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * @Description: 让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
     * @Param: [key, start, end]
     * @return: void
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:19
     */
    public void lTrim(String key, long start, long end) {
        if (key == null) {
            return;
        }
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * @Description: 该命令将返回链表中指定位置(index)的元素，index是0-based，表示从头部位置开始第index的元素，
     * 如果index为-1，表示尾部元素。如果与该Key关联的不是链表，该命令将返回相关的错误信息。 如果超出index返回这返回nil
     * @Param: [key, index]
     * @return: java.lang.Object
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:20
     */
    public Object lIndex(String key, long index) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * @Description: 返回指定key关联的队列中的元素数量
     * @Param: [key]
     * @return: java.lang.Long
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:24
     */
    public Long lLen(String key) {
        if (null == key) {
            return 0L;
        }
        return redisTemplate.opsForList().size(key);
    }
    /** *************Set数据类型************* */
    /**
     * @Description: 向set中插入
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:24
     */
    public Long sAdd(String key, Object... members) {
        if (null == key) {
            return 0L;
        }
        return redisTemplate.opsForSet().add(key, members);
    }

    /**
     * @Description: 返回set成员数量
     * @Param: [key]
     * @return: java.lang.Long
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:26
     */
    public Long sCard(String key) {
        if (null == key) {
            return 0L;
        }
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * @Description: 判断是否是成员
     * @Param: [key, member]
     * @return: java.lang.Boolean
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:26
     */
    public Boolean sIsMember(String key, Object member) {
        if (null == key) {
            return false;
        }
        return redisTemplate.opsForSet().isMember(key, member);
    }

    /**
     * @Description: 随机返回一个成员
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:27
     */
    public Object sRandomMember(String key) {
        if (null == key) {
            return null;
        }
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * @Description: 返回指定数量的成员
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:28
     */
    public List<Object> sRandomMember(String key, int count) {
        if (null == key) {
            return null;
        }
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * @Description: 随机删除一个member并返回
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:28
     */
    public Object sPop(String key) {
        if (null == key) {
            return null;
        }
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * @Description: 获取所有member
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:30
     */
    public Set<Object> sMembers(String key) {
        if (null == key) {
            return null;
        }
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * @Description: 从与Key关联的Set中删除参数中指定的成员，不存在的参数成员将被忽略，
     * 如果该Key并不存在，将视为空Set处理。返回从Set中实际移除的成员数量，如果没有则返回0
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:30
     */
    public Long sRem(String key, Object... members) {
        if (null == key) {
            return 0L;
        }
        return redisTemplate.opsForSet().remove(key, members);
    }

    /**
     * @Description: 将一个集合中的元素移动到另外一个集合中去
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:31
     */
    public Boolean sMove(String srckey, String dstkey, Object member) {
        if (null == srckey || null == dstkey) {
            return false;
        }
        return redisTemplate.opsForSet().move(srckey, member, dstkey);
    }

    /**
     * @Description: 获取两个集合的并集
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:31
     */
    public Set<Object> sUnion(String key, String otherKeys) {
        if (null == key || otherKeys == null) {
            return null;
        }
        return redisTemplate.opsForSet().union(key, otherKeys);
    }
//**********Sorted Set 数据类型********************

    /**
     * @Description: 添加参数中指定的所有成员及其分数到指定key的Sorted Set中，在该命令中我们可以指定多组score/member作为参数。
     * 如果在添加时参数中的某一成员已经存在，该命令将更新此成员的分数为新值，同时再将该成员基于新值重新排序。
     * 如果键不存在，该命令将为该键创建一个新的Sorted Set Value，并将score/member对插入其中。
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:32
     */
    public Boolean zAdd(String key, double score, Object member) {
        if (null == key) {
            return false;
        }
        return redisTemplate.opsForZSet().add(key, member, score);
    }

    /**
     * @Description: 该命令将移除参数中指定的成员，其中不存在的成员将被忽略。
     * 如果与该Key关联的Value不是Sorted Set，相应的错误信息将被返回。 如果操作成功则返回实际被删除的成员数量
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:32
     */
    public Long zRem(String key, Object... members) {
        if (null == key || null == members) {
            return 0L;
        }
        return redisTemplate.opsForZSet().remove(key, members);
    }

    /**
     * @Description: 返回Sorted Set中的成员数量，如果该Key不存在，返回0。
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:33
     */
    public Long zCard(String key) {
        if (null == key) {
            return 0L;
        }
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * @Description: 该命令将为指定Key中的指定成员增加指定的分数。如果成员不存在，该命令将添加该成员并假设其初始分数为0，
     * 此后再将其分数加上increment。如果Key不存在，该命令将创建该Key及其关联的Sorted Set，
     * 并包含参数指定的成员，其分数为increment参数。如果与该Key关联的不是Sorted Set类型，
     * 相关的错误信息将被返回。如果不报错则以串形式表示的新分数。
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:34
     */
    public Double zIncrby(String key, double score, Object member) {
        if (null == key) {
            throw new IllegalArgumentException("zIncrby key 不能为空");
        }
        return redisTemplate.opsForZSet().incrementScore(key, member, score);
    }

    /**
     * @Description: 该命令用于获取分数(score)在min和max之间的成员数量。
     * （min=<score<=max）如果加上了“(”着表明是开区间例如zcount key (min max 则 表示（min<score=<max）
     * 同理zcount key min (max 则表明(min=<score<max) 返回指定返回数量。
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:37
     */
    public Long zCount(String key, double min, double max) {
        if (null == key) {
            return 0L;
        }
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * @Description: Sorted Set中的成员都是按照分数从低到高的顺序存储，该命令将返回参数中指定成员的位置值，
     * 其中0表示第一个成员，它是Sorted Set中分数最低的成员。 如果该成员存在，则返回它的位置索引值。否则返回nil。
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:37
     */
    public Long zRank(String key, Object member) {
        if (null == key) {
            return null;
        }
        return redisTemplate.opsForZSet().rank(key, member);
    }

    /**
     * @Description: 如果该成员存在，以字符串的形式返回其分数，否则返回null
     * @Param:
     * @return:
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:37
     */
    public Double zScore(String key, Object member) {
        if (null == key) {
            return null;
        }
        return redisTemplate.opsForZSet().score(key, member);
    }

    /**
     * @Description: 该命令返回顺序在参数start和stop指定范围内的成员，这里start和stop参数都是0-based，即0表示第一个成员，-1表示最后
     * 一个成员。如果start大于该Sorted
     * Set中的最大索引值，或start > stop，此时一个空集合将被返回。如果stop大于最大索引值，
     * 该命令将返回从start到集合的最后一个成员。如果命令中带有可选参数WITHSCORES选项，
     * 该命令在返回的结果中将包含每个成员的分数值，如value1,score1,value2,score2...。
     * @Param: [key, min, max]
     * @return: java.util.Set<java.lang.Object>
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:38
     */
    public Set<Object> zRange(String key, long min, long max) {
        if (null == key) {
            return null;
        }
        return redisTemplate.opsForZSet().range(key, min, max);
    }

    /**
     * @Description: 该命令的功能和ZRANGE基本相同，唯一的差别在于该命令是通过反向排序获取指定位置的成员，
     * 即从高到低的顺序。如果成员具有相同的分数，则按降序字典顺序排序。
     * @Param: [key, start, end]
     * @return: java.util.Set<java.lang.Object>
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:38
     */
    public Set<Object> zReverseRange(String key, long start, long end) {
        if (null == key) {
            return null;
        }
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * @Description: 该命令将返回分数在min和max之间的所有成员，即满足表达式min <= score <= max的成员，
     * 其中返回的成员是按照其分数从低到高的顺序返回，如果成员具有相同的分数，
     * 则按成员的字典顺序返回。可选参数LIMIT用于限制返回成员的数量范围。
     * 可选参数offset表示从符合条件的第offset个成员开始返回，同时返回count个成员。
     * 可选参数WITHSCORES的含义参照ZRANGE中该选项的说明。*最后需要说明的是参数中min和max的规则可参照命令ZCOUNT。
     * @Param: [key, min, max]
     * @return: java.util.Set<java.lang.Object>
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:39
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        if (null == key) {
            return null;
        }

        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * @Description: 该命令除了排序方式是基于从高到低的分数排序之外，其它功能和参数含义均与ZRANGEBYSCORE相同。
     * 需要注意的是该命令中的min和max参数的顺序和ZRANGEBYSCORE命令是相反的。
     * @Param: [key, min, max]
     * @return: java.util.Set<java.lang.Object>
     * @Author: MiZiMi
     * @Date: 2020/4/28 18:39
     */
    public Set<Object> zReverseRangeByScore(String key, double min, double max) {
        if (null == key) {
            return null;
        }
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }
}

