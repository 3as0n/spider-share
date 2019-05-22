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

package com.datatrees.spider.share.service.dao;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Deprecated
public interface RedisDao {

    boolean saveListString(final String key, final List<String> valueList);

    boolean saveString2List(final String key, final String value);

    String getStringFromList(final String key);

    boolean pushMessage(String submitRedisKey, String messageType);

    boolean pushMessage(String submitRedisKey, String messageType, int ttlSeconds);

    String pullResult(String obtainRedisKey);

    RedisTemplate<String, String> getRedisTemplate();

    void deleteKey(String key);

    /**
     * key的值+1,并返回增加后的值
     * 
     * @param key 如果没有,设置为1
     * @return
     */
    Long increaseAndGet(String key);
}
