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

package com.datatrees.spider.share.common.share.service;

import com.alibaba.fastjson.TypeReference;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.domain.directive.DirectiveResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis操作 Created by zhouxinghai on 2017/5/23
 */
@Deprecated
public interface RedisService {

    /**
     * 保存
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean saveBytes(String key, byte[] value);

    /**
     * @param key
     * @return
     */
    public byte[] getBytes(String key);

    /**
     * 是否包含key
     * 
     * @param key
     * @return
     */
    public boolean hasKey(String key);

    /**
     * 删除key
     * 
     * @param key
     * @return
     */
    public boolean deleteKey(String key);

    /**
     * redis加锁
     * 
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public boolean lock(String key, long timeout, TimeUnit unit);

    /**
     * redis解锁
     * 
     * @param key
     * @return
     */
    public boolean unlock(String key);

    /**
     * 获取
     * 
     * @param key
     * @return
     */
    public String getString(String key);

    /**
     * 获取,有超时时间
     */
    public String getString(RedisKeyPrefixEnum redisKeyPrefixEnum, Object postfix);

    /**
     * 获取,有超时时间
     * 
     * @param key
     * @param timeout
     * @param timeUnit
     * @return
     */
    public String getString(String key, long timeout, TimeUnit timeUnit);

    /**
     * 从list取最后一个值
     * 
     * @param key
     * @return
     */
    public String rightPop(String key);

    /**
     * 从list取最后一个值
     * 
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public String rightPop(String key, long timeout, TimeUnit unit);

    /**
     * 保存
     * 
     * @param key
     * @param value
     * @param timeout 过期时间
     * @param unit 过期时间单位
     * @return
     */
    public boolean saveString(String key, String value, long timeout, TimeUnit unit);

    /**
     * 保存
     */
    public boolean saveString(RedisKeyPrefixEnum redisKeyPrefixEnum, Object postfix, String value);

    /**
     * 保存到list
     * 
     * @param key
     * @param value
     * @param timeout 过期时间
     * @param unit 过期时间单位
     * @return
     */
    public boolean saveToList(String key, String value, long timeout, TimeUnit unit);

    /**
     * 保存到list
     * 
     * @param key
     * @param list
     * @param timeout 过期时间
     * @param unit 过期时间单位
     * @return
     */
    public boolean saveToList(String key, List<String> list, long timeout, TimeUnit unit);

    /**
     * 保存交互指令 保存到指令池和单条指令
     * 
     * @param result
     * @return
     */
    public String saveDirectiveResult(DirectiveResult result);

    /**
     * 保存交互指令 保存到指令池和单条指令
     * 
     * @param directiveId 指令ID,作为redisKey
     * @param result
     * @return
     */
    public String saveDirectiveResult(String directiveId, DirectiveResult result);

    /**
     * 获取还未执行的最后一条指令
     * 
     * @param groupKey 指令池key
     * @return
     */
    public <T> DirectiveResult<T> getNextDirectiveResult(String groupKey, long timeout, TimeUnit timeUnit);

    /**
     * 获取,有超时时间
     * 
     * @param directiveKey 单条指令key
     * @param timeout
     * @param timeUnit
     * @return
     */
    public <T> DirectiveResult getDirectiveResult(String directiveKey, long timeout, TimeUnit timeUnit);

    /**
     * 创建指令ID
     * 
     * @param appName 项目名称
     * @return 指令ID
     */
    public String createDirectiveId(String appName);

    /**
     * 创建指令ID
     * 
     * @return 指令ID
     */
    public String createDirectiveId();

    /**
     * 缓存 value:转化成json
     * 
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    public void cache(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 缓存 value:转化成json
     * 
     * @param redisKeyPrefixEnum 前缀
     * @param postfix 后缀
     * @param value 值
     */
    public void cache(RedisKeyPrefixEnum redisKeyPrefixEnum, Object postfix, Object value);

    /**
     * 缓存
     * 
     * @param redisKeyPrefixEnum 前缀
     * @param value 值
     */
    public void cache(RedisKeyPrefixEnum redisKeyPrefixEnum, Object value);

    /**
     * 查找缓存
     * 
     * @param key
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> T getCache(String key, TypeReference<T> typeReference);

    /**
     * 查找缓存
     * 
     * @param redisKeyPrefixEnum
     * @param <T>
     * @return
     */
    public <T> T getCache(RedisKeyPrefixEnum redisKeyPrefixEnum, TypeReference<T> typeReference);

    /**
     * 查找缓存
     * 
     * @param redisKeyPrefixEnum
     * @param postfix
     * @param <T>
     * @return
     */
    public <T> T getCache(RedisKeyPrefixEnum redisKeyPrefixEnum, Object postfix, TypeReference<T> typeReference);

    /**
     * 加锁 有效期5秒 5秒没有完成锁定,返回false
     * 
     * @param redisKey
     */
    Boolean lock(Object redisKey);

    /**
     * 加锁 有效期5秒 5秒没有完成锁定,抛出异常
     * 
     * @param redisKey
     */
    void lockFailThrowException(Object redisKey);

    /**
     * 解锁
     * 
     * @param redisKey
     */
    void unLock(Object redisKey);

    <K, V> Map<K, V> getMap(String redisKey);

    <K, V> void putMap(String redisKey, Map<K, V> map);

    <K, V> void putMap(String redisKey, K key, V value);
}
