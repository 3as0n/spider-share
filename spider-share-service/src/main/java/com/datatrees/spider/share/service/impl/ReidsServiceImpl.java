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

package com.datatrees.spider.share.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.datatrees.spider.share.common.share.service.RedisService;
import com.datatrees.spider.share.common.utils.CheckUtils;
import com.datatrees.spider.share.common.utils.DateUtils;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.domain.directive.DirectiveResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ReidsServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(ReidsServiceImpl.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 默认超时时间(单位:秒),默认1小时
     */
    @Value("${rawdatacentral.redisKey.timeout:3600}")
    private long defaultTimeOut;

    @Override
    public boolean saveBytes(String key, byte[] value) {
        CheckUtils.checkNotBlank(key, "key is blank");
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    @Override
    public byte[] getBytes(String key) {
        CheckUtils.checkNotBlank(key, "key is blank");
        return (byte[])redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean hasKey(String key) {
        if (StringUtils.isBlank(key)) {
            logger.warn("hasKey error,invalid param key is blank key={}", key);
            return false;
        }
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("redis hasKey error key={}", key, e);
            return false;
        }
    }

    @Override
    public boolean deleteKey(String key) {
        if (StringUtils.isBlank(key)) {
            logger.warn("deleteKey fail,invalid param key is blank key={}", key);
            return false;
        }
        try {
            if (stringRedisTemplate.hasKey(key)) {
                stringRedisTemplate.delete(key);
                logger.info("deleteKey success key={}", key);
            }
            return true;
        } catch (Exception e) {
            logger.error("deleteKey error key={}", key, e);
            return false;
        }
    }

    @Override
    public boolean lock(String key, long timeout, TimeUnit unit) {
        if (StringUtils.isBlank(key)) {
            logger.warn("lodk fail,invalid param key is blank key={}", key);
            return false;
        }
        try {
            boolean b = stringRedisTemplate.opsForValue().setIfAbsent(key, key);
            if (!b) {
                logger.warn("lock fail key={},timeout={},unit={}", key, timeout, unit);
                return false;
            }
            stringRedisTemplate.expire(key, timeout, unit);
            logger.info("lock success key={},timeout={},unit={}", key, timeout, unit);
            return true;
        } catch (Exception e) {
            logger.error("lock error key={},timeout={},unit={}", key, timeout, unit, e);
            return false;
        }
    }

    @Override
    public boolean unlock(String key) {
        if (StringUtils.isBlank(key)) {
            logger.warn("unlock fail, invalid param key is blank key={}", key);
            return false;
        }
        boolean b = deleteKey(key);
        logger.info("unlock {} key={}", b ? "success" : "fail", key);
        return b;
    }

    @Override
    public String getString(String key) {
        if (!hasKey(key)) {
            return null;
        }
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            return cleanJson(value);
        } catch (Exception e) {
            logger.error("getString error key={}", key, e);
            return null;
        }
    }

    @Override
    public String getString(RedisKeyPrefixEnum redisKeyPrefixEnum, Object postfix) {
        return getString(redisKeyPrefixEnum.getRedisKey(postfix), redisKeyPrefixEnum.getTimeout(), redisKeyPrefixEnum.getTimeUnit());
    }

    @Override
    public String getString(String key, long timeout, TimeUnit timeUnit) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        long startTime = System.currentTimeMillis();
        long wait = timeUnit.toMillis(timeout);
        long endTime = startTime + wait;
        long sleeptime = wait >= 300 ? 300 : wait;
        try {
            logger.info("getString wait {}s, key={}", timeUnit.toSeconds(timeout), key);
            do {
                TimeUnit.MILLISECONDS.sleep(sleeptime);
                if (stringRedisTemplate.hasKey(key)) {
                    String value = stringRedisTemplate.opsForValue().get(key);
                    logger.info("getString success,useTime={}, key={}", DateUtils.getUsedTime(startTime, System.currentTimeMillis()), key);
                    return cleanJson(value);
                }
            } while (System.currentTimeMillis() <= endTime);
            logger.warn("getString fail,useTime={}, key={}", DateUtils.getUsedTime(startTime, System.currentTimeMillis()), key);
        } catch (Exception e) {
            logger.error("getString error,useTime={}, key={}", DateUtils.getUsedTime(startTime, System.currentTimeMillis()), key, e);
        }
        return null;
    }

    @Override
    public String rightPop(String key) {
        if (!hasKey(key)) {
            return null;
        }
        try {
            return stringRedisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            logger.error("rightPop error key={}", key, e);
            return null;
        }
    }

    @Override
    public String rightPop(String key, long timeout, TimeUnit timeUnit) {
        if (!hasKey(key)) {
            return null;
        }
        long startTime = System.currentTimeMillis();
        long wait = timeUnit.toMillis(timeout);
        long endTime = startTime + wait;
        long sleeptime = wait >= 300 ? 300 : wait;
        try {
            logger.info("rightPop wait {}s, key={}", timeUnit.toSeconds(timeout), key);
            do {
                TimeUnit.MILLISECONDS.sleep(sleeptime);
                if (stringRedisTemplate.hasKey(key)) {
                    String value = stringRedisTemplate.opsForList().rightPop(key);
                    logger.info("rightPop success,useTime={}, key={}", DateUtils.getUsedTime(startTime, System.currentTimeMillis()), key);
                    return value;
                }
            } while (System.currentTimeMillis() <= endTime);
            logger.warn("rightPop fail,useTime={},key={}", DateUtils.getUsedTime(startTime, System.currentTimeMillis()), key);
        } catch (Exception e) {
            logger.error("rightPop error,useTime={},key={}", DateUtils.getUsedTime(startTime, System.currentTimeMillis()), key, e);
        }
        return null;
    }

    @Override
    public boolean saveString(String key, String value, long timeout, TimeUnit unit) {
        if (StringUtils.isBlank(key) || null == value) {
            throw new RuntimeException("saveString invalid param key or value");
        }
        if (timeout <= 0) {
            throw new RuntimeException("saveString invalid param");
        }
        try {
            stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            logger.error("saveString error key={}", key, e);
            return false;
        }
    }

    @Override
    public boolean saveString(RedisKeyPrefixEnum redisKeyPrefixEnum, Object postfix, String value) {
        return saveString(redisKeyPrefixEnum.getRedisKey(postfix), value, redisKeyPrefixEnum.getTimeout(), redisKeyPrefixEnum.getTimeUnit());
    }

    @Override
    public boolean saveToList(String key, String value, long timeout, TimeUnit unit) {
        try {
            if (StringUtils.isAnyBlank(key, value)) {
                throw new RuntimeException("saveToList invalid param key or value");
            }
            stringRedisTemplate.opsForList().rightPush(key, value);
            stringRedisTemplate.expire(key, timeout, unit);
            logger.info("saveToList success key={}", key);
            return true;
        } catch (Exception e) {
            logger.error("saveToList error key={}", key, e);
            return false;
        }
    }

    @Override
    public boolean saveToList(String key, List<String> list, long timeout, TimeUnit unit) {
        try {
            if (StringUtils.isBlank(key) || null == list) {
                throw new RuntimeException("saveToList invalid param key or value");
            }
            if (list.isEmpty()) {
                logger.info("saveToList success key={},list is empty", key);
                return true;
            }
            stringRedisTemplate.opsForList().rightPushAll(key, list);
            stringRedisTemplate.expire(key, timeout, unit);
            logger.info("saveToList success key={}", key);
            return true;
        } catch (Exception e) {
            logger.error("saveToList error key={}", key, e);
            return false;
        }
    }

    @Override
    public String saveDirectiveResult(DirectiveResult result) {
        if (null == result) {
            throw new RuntimeException("saveDirectiveResult error param is null");
        }
        String directiveId = result.getDirectiveId();
        if (null == directiveId) {
            directiveId = createDirectiveId();
            result.setDirectiveId(directiveId);
        }
        String json = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
        // TODO加入事物控制
        saveToList(result.getGroupKey(), json, defaultTimeOut, TimeUnit.SECONDS);
        saveString(result.getDirectiveKey(), json, defaultTimeOut, TimeUnit.SECONDS);
        logger.info("saveDirectiveResult success,groupKey={},directiveKey={},directiveId={}", result.getGroupKey(), result.getDirectiveKey(), directiveId);
        return directiveId;
    }

    @Override
    public String saveDirectiveResult(String directiveId, DirectiveResult result) {
        if (null == result) {
            throw new RuntimeException("saveDirectiveResult error param is null");
        }
        result.setDirectiveId(directiveId);
        String json = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
        // TODO加入事物控制
        // saveToList(result.getGroupKey(), json, Constants.REDIS_KEY_TIMEOUT, TimeUnit.SECONDS);
        saveString(directiveId, json, defaultTimeOut, TimeUnit.SECONDS);
        logger.info("saveDirectiveResult success,directiveKey={},directiveId={}", directiveId, directiveId);
        return directiveId;
    }

    @Override
    public <T> DirectiveResult<T> getNextDirectiveResult(String groupKey, long timeout, TimeUnit timeUnit) {
        if (StringUtils.isBlank(groupKey)) {
            throw new RuntimeException("getDirectiveResult error key is blank");
        }
        String value = rightPop(groupKey, timeout, timeUnit);
        if (StringUtils.isNotBlank(value)) {
            logger.info("getNextDirectiveResult success groupKey={}", groupKey);
            return JSON.parseObject(value, new TypeReference<DirectiveResult<T>>() {});
        }
        logger.info("getNextDirectiveResult fail groupKey={}", groupKey);
        return null;
    }

    @Override
    public <T> DirectiveResult<T> getDirectiveResult(String directiveKey, long timeout, TimeUnit timeUnit) {
        String value = getString(directiveKey, timeout, timeUnit);
        if (StringUtils.isNoneBlank(value)) {
            logger.info("getDirectiveResult success directiveKey={}", directiveKey);
            return JSON.parseObject(value, new TypeReference<DirectiveResult<T>>() {});
        }
        logger.info("getDirectiveResult fail directiveKey={}", directiveKey);
        return null;
    }

    @Override
    public String createDirectiveId(String appName) {
        if (StringUtils.isBlank(appName)) {
            throw new RuntimeException("createDirectiveId error appName is blank");
        }
        String key = "directive_id_" + appName;
        if (!stringRedisTemplate.hasKey(appName)) {
            stringRedisTemplate.opsForValue().setIfAbsent(key, "1");
        }
        return appName + "_" + stringRedisTemplate.opsForValue().increment(key, 1);
    }

    @Override
    public String createDirectiveId() {
        return createDirectiveId(DirectiveResult.getAppName());
    }

    @Override
    public void cache(String key, Object value, long timeout, TimeUnit unit) {
        if (StringUtils.isBlank(key) || null == value) {
            logger.error("invalid param key={} or value={}", key, value);
            throw new RuntimeException("invalid param key or value");
        }
        String json = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect);
        saveString(key, json, timeout, unit);
        logger.info("cache success key={}", key);
    }

    @Override
    public void cache(RedisKeyPrefixEnum redisKeyPrefixEnum, Object postfix, Object value) {
        cache(redisKeyPrefixEnum.getRedisKey(postfix), value, redisKeyPrefixEnum.getTimeout(), redisKeyPrefixEnum.getTimeUnit());

    }

    @Override
    public void cache(RedisKeyPrefixEnum redisKeyPrefixEnum, Object value) {
        cache(redisKeyPrefixEnum.getRedisKey(), value, redisKeyPrefixEnum.getTimeout(), redisKeyPrefixEnum.getTimeUnit());
    }

    @Override
    public <T> T getCache(String key, TypeReference<T> typeReference) {
        String json = getStringWithNoClean(key);
        if (StringUtils.isNoneBlank(json)) {
            T result = JSON.parseObject(json, typeReference);
            logger.info("getCache success key={}", key);
            return result;
        }
        return null;
    }

    @Override
    public <T> T getCache(RedisKeyPrefixEnum redisKeyPrefixEnum, TypeReference<T> typeReference) {
        return getCache(redisKeyPrefixEnum.getRedisKey(), typeReference);
    }

    @Override
    public <T> T getCache(RedisKeyPrefixEnum redisKeyPrefixEnum, Object postfix, TypeReference<T> typeReference) {
        return getCache(redisKeyPrefixEnum.getRedisKey(postfix), typeReference);
    }

    @Override
    public Boolean lock(Object redisKey) {
        long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
        String lockKey = RedisKeyPrefixEnum.LOCK.getRedisKey(redisKey.toString());
        boolean result;
        while (System.currentTimeMillis() < endTime) {
            // TODO: 2018/5/3 死锁风险。比如：设置成功值后，系统崩溃或redis连接异常导致超时设置失败。
            result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "locked");
            if (result) {
                stringRedisTemplate.expire(lockKey, RedisKeyPrefixEnum.LOCK.getTimeout(), RedisKeyPrefixEnum.LOCK.getTimeUnit());
                logger.info("lock success redisKey={}", redisKey);
                return true;
            }
        }
        logger.error("lock fail redisKey={}", redisKey);
        return false;
    }

    @Override
    public void lockFailThrowException(Object redisKey) {
        if (!lock(redisKey)) {
            throw new RuntimeException("lock fail redisKey=" + redisKey);
        }
    }

    @Override
    public void unLock(Object redisKey) {
        String lockKey = RedisKeyPrefixEnum.LOCK.getRedisKey(redisKey.toString());
        deleteKey(lockKey);
        logger.info("unlock success redisKey={}", redisKey);
    }

    @Override
    public <K, V> Map<K, V> getMap(String redisKey) {
        return redisTemplate.opsForHash().entries(redisKey);
    }

    @Override
    public <K, V> void putMap(String redisKey, Map<K, V> map) {
        redisTemplate.opsForHash().putAll(redisKey, map);
    }

    @Override
    public <K, V> void putMap(String redisKey, K key, V value) {
        redisTemplate.opsForHash().put(redisKey, key, value);
    }

    public String getStringWithNoClean(String key) {
        if (!hasKey(key)) {
            return null;
        }
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("getString error key={}", key, e);
            return null;
        }
    }

    public String cleanJson(String json) {
        if (StringUtils.isBlank(json)) {
            return json;
        }
        if (json.startsWith("\"") && json.endsWith("\"")) {
            return json.substring(1, json.length() - 1);
        }
        return json;
    }

}
