/*
 * Copyright © 2015 - 2019 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.datatrees.spider.share.common.utils;

import com.datatrees.spider.share.common.config.RedisConfig;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisUtils {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    private static JedisPool jedisPool;

    private static RedisConfig redisConfig = new RedisConfig();

    public static void init() {
        try {
            if (null == jedisPool) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(redisConfig.getMaxTotal());
                config.setMaxIdle(redisConfig.getMaxIdle());
                config.setMinIdle(redisConfig.getMinIdle());
                config.setMaxWaitMillis(redisConfig.getMaxWait());
                config.setTestOnBorrow(true);
                if (StringUtils.equals("", redisConfig.getPassword())) {
                    redisConfig.setPassword(null);
                }
                jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), redisConfig.getPassword(), redisConfig.getDatabase());
            }

        } catch (Exception e) {
            throw new RuntimeException("redis init error ");
        }
    }

    public static void init(String host, int port, String password, int datebase) {
        try {
            redisConfig.setHost(host);
            redisConfig.setPort(port);
            redisConfig.setPassword(password);
            redisConfig.setDatabase(datebase);
            init();
        } catch (Exception e) {
            throw new RuntimeException("redis init error ");
        }
    }

    /**
     * 模糊匹配redis的key
     * 
     * @param pattern 模糊key 例：redis.key*
     * @return
     */
    public static Set<String> keys(final String pattern) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys(pattern);
        }
    }

    public static Boolean exists(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }
    }

    public static Boolean del(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long i = jedis.del(key);
            return i >= 1;
        }
    }

    public static Long expire(final String key, final int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.expire(key, second);
        }
    }

    public static Boolean hexists(final String key, final String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hexists(key, field);
        }
    }

    public static String type(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.type(key);
        }
    }

    public static String get(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public static byte[] getForByte(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key.getBytes());
        }
    }

    public static Map<String, String> hgetAll(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    public static String hget(final String key, final String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        }
    }

    public static byte[] hgetForByte(final String key, final String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key.getBytes(), field.getBytes());
        }
    }

    public static long hdel(final String key, final String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hdel(key, field);
        }
    }

    public static String set(final String key, final String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        }
    }

    public static String set(final String key, final String value, final Integer second) {
        try (Jedis jedis = jedisPool.getResource()) {
            String r = jedis.set(key, value);
            expire(key, second);
            return r;
        }
    }

    public static Long hset(final String key, final String field, final String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hset(key, field, value);
        }
    }

    public static String setex(RedisKeyPrefixEnum keyEnum, Object prefix, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.psetex(keyEnum.getRedisKey(prefix), keyEnum.getTimeUnit().toMillis(keyEnum.getTimeout()), value);
        }
    }

    /**
     * 如果不存在,设置value
     * 
     * @param key
     * @param value
     * @return
     */
    public static Boolean setnx(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long r = jedis.setnx(key, value);
            return r == 1;
        }
    }

    /**
     * 如果不存在,设置value
     * 
     * @param key
     * @param value
     * @param second 失效时间(单位:秒)
     * @return
     */
    public static Boolean setnx(String key, String value, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            boolean b = setnx(key, value);
            if (b) {
                jedis.expire(key, second);
            }
            return b;
        }
    }

    public static String get(String key, int waitSecond) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        long startTime = System.currentTimeMillis();
        long wait = TimeUnit.SECONDS.toMillis(waitSecond);
        long endTime = startTime + wait;
        long sleeptime = wait >= 200 ? 200 : wait;
        try (Jedis jedis = jedisPool.getResource()) {
            logger.info("get from redis wait {}s, key={}", waitSecond, key);
            do {
                TimeUnit.MILLISECONDS.sleep(sleeptime);
                if (jedis.exists(key)) {
                    String value = jedis.get(key);
                    logger.info("getString success,useTime={}, key={}", DateUtils.getUsedTime(startTime, System.currentTimeMillis()), key);
                    return cleanJson(value);
                }
            } while (System.currentTimeMillis() <= endTime);
            logger.warn("getString fail,useTime={}, key={}", DateUtils.getUsedTime(startTime, System.currentTimeMillis()), key);
        } catch (Throwable e) {
            logger.error("getString error,useTime={}, key={}", DateUtils.getUsedTime(startTime, System.currentTimeMillis()), key, e);
        }
        return null;
    }

    public static void hset(String key, String name, String value, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, name, value);
            jedis.expire(key, second);
        }
    }

    public static long hincrBy(String key, String name, long value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hincrBy(key, name, value);
        }
    }

    public static void hset(String key, String name, byte[] value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key.getBytes(), name.getBytes(), value);
        }
    }

    public static void hset(String key, String name, String value, long timeout, TimeUnit unit) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, name, value);
            jedis.expire(key, toSeconds(timeout, unit));
        }
    }

    public static Boolean lock(Object redisKey, int second) {
        long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(second);
        String lockKey = "lock." + redisKey.toString();
        boolean result = false;
        while (!result && System.currentTimeMillis() < endTime) {
            result = setnx(lockKey, "true", second);
            if (result) {
                logger.info("lock success redisKey={}", redisKey);
                return true;
            }
        }
        logger.error("lock fail redisKey={}", redisKey);
        return false;
    }

    public static void lockFailThrowException(Object redisKey, int second) {
        if (!lock(redisKey, second)) {
            throw new RuntimeException("lock fail redisKey=" + redisKey);
        }
    }

    public static Boolean unLock(Object redisKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            String lockKey = "lock." + redisKey.toString();
            jedis.del(lockKey);
            logger.info("unlock success redisKey={}", redisKey);
            return true;
        }
    }

    public static String cleanJson(String json) {
        if (StringUtils.isBlank(json)) {
            return json;
        }
        if (json.startsWith("\"") && json.endsWith("\"")) {
            return json.substring(1, json.length() - 1);
        }
        return json;
    }

    public static int toSeconds(long timeout, TimeUnit unit) {
        long l = unit.toSeconds(timeout);
        return l > 0 ? (int)l : 1;
    }

    public static long llen(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.llen(key);
        }
    }

    public static List<String> lrange(String key, long start, long end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, start, end);
        }
    }

    public static long rpush(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpush(key, value);
        }
    }

    public static String set(byte[] key, byte[] value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        }
    }

    public static byte[] get(byte[] key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public static long incr(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incr(key);
        }
    }

    public static void sadd(final String key, String value, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.sadd(key, value);
            jedis.expire(key, seconds);
        }
    }

    public static void sadd(final String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.sadd(key, value);
        }
    }

    public static Set<String> smembers(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.smembers(key);
        }
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        RedisUtils.redisConfig = redisConfig;
    }

}
