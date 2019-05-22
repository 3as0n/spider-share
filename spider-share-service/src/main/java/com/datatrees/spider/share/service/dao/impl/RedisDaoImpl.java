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

package com.datatrees.spider.share.service.dao.impl;

import com.datatrees.spider.share.service.constants.Constants;
import com.datatrees.spider.share.service.dao.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDaoImpl implements RedisDao {

    private static final Logger logger = LoggerFactory.getLogger(RedisDaoImpl.class);

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean saveString2List(final String key, final String value) {
        List list = new ArrayList<String>();
        list.add(value);
        return saveListString(key, list);
    }

    @Override
    public boolean saveListString(final String key, final List<String> valueList) {
        Long result = redisTemplate.opsForList().rightPushAll(key, valueList.toArray(new String[valueList.size()]));
        redisTemplate.expire(key, Constants.REDIS_KEY_TIMEOUT, TimeUnit.SECONDS);
        return result != null ? true : false;
    }

    @Override
    public String getStringFromList(final String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public String pullResult(final String obtainRedisKey) {
        try {
            return redisTemplate.opsForValue().get(obtainRedisKey);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean pushMessage(final String submitRedisKey, final String messageType) {
        return this.pushMessage(submitRedisKey, messageType, Constants.REDIS_KEY_TIMEOUT);
    }

    @Override
    public boolean pushMessage(String submitRedisKey, String messageType, int ttlSeconds) {
        try {
            redisTemplate.opsForValue().set(submitRedisKey, messageType);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        redisTemplate.expire(submitRedisKey, ttlSeconds, TimeUnit.SECONDS);
        return true;
    }

    /**
     * @return the redisTemplate
     */
    @Override
    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    /*
     * (non-Javadoc)
     *
     * @see RedisDao#deleteKey(java.lang.String)
     */
    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Long increaseAndGet(String key) {
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().setIfAbsent(key, "0");
        }
        return redisTemplate.opsForValue().increment(key, 1);
    }

}
