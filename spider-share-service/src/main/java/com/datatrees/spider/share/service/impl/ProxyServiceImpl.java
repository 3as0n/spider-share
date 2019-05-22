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
import com.datatrees.spider.share.common.share.service.ProxyService;
import com.datatrees.spider.share.common.share.service.RedisService;
import com.datatrees.spider.share.common.utils.CheckUtils;
import com.datatrees.spider.share.common.utils.RedisUtils;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.treefinance.proxy.api.ProxyProvider;
import com.treefinance.proxy.domain.IpLocale;
import com.treefinance.proxy.domain.Proxy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProxyServiceImpl implements ProxyService {

    private static final Logger logger = LoggerFactory.getLogger(ProxyServiceImpl.class);

    @Resource
    private ProxyProvider proxyProvider;

    @Resource
    private RedisService redisService;

    @Override
    public Proxy getProxy(Long taskId, String websiteName) {
        CheckUtils.checkNotPositiveNumber(taskId, ErrorCode.EMPTY_TASK_ID.getErrorMsg());
        CheckUtils.checkNotBlank(websiteName, ErrorCode.EMPTY_WEBSITE_NAME);
        Proxy proxy = null;
        try {
            String proxyString = RedisUtils.get(RedisKeyPrefixEnum.WEBSITE_PROXY.getRedisKey(websiteName));
            if (StringUtils.isNotBlank(proxyString)) {
                proxy = new Proxy();
                proxy.setIp(proxyString.split(":")[0]);
                proxy.setPort(proxyString.split(":")[1]);
                logger.warn("危险操作:指定的代理可能不能用,taskId={},websiteName={},proxyString={}", taskId, websiteName, proxyString);
                return proxy;
            }
            proxy = redisService.getCache(RedisKeyPrefixEnum.TASK_PROXY.getRedisKey(taskId), new TypeReference<Proxy>() {});
            if (null != proxy) {
                // ip为空不再访问dubbo接口,第一次调用没有取到proxy,中途不更换
                return StringUtils.isBlank(proxy.getIp()) ? null : proxy;
            }
            proxy = getProxyFromDubbo(taskId, websiteName);
            if (null != proxy) {
                redisService.cache(RedisKeyPrefixEnum.TASK_PROXY, taskId, proxy);
                return proxy;
            }
            redisService.cache(RedisKeyPrefixEnum.TASK_PROXY, taskId, new Proxy());
            return null;
        } catch (Exception e) {
            logger.error("getProxy error taskId={},websiteName={}", taskId, websiteName, e);
            return null;
        }

    }

    @Override
    public void release(Long taskId) {
        try {
            proxyProvider.release(taskId);
            redisService.deleteKey(RedisKeyPrefixEnum.TASK_PROXY.getRedisKey(taskId));
            logger.info("Succeed to release proxy for task: {}", taskId);
        } catch (Exception e) {
            logger.error("Error releasing proxy for task: {}", taskId, e);
        }
    }

    @Override
    public void clear(Long taskId) {
        try {
            release(taskId);
            RedisUtils.del(RedisKeyPrefixEnum.TASK_PROXY_ENABLE.getRedisKey(taskId));
        } catch (Exception e) {
            logger.error("Error clear proxy info for task: {}", taskId, e);
        }
    }

    private Proxy getProxyFromDubbo(Long taskId, String websiteName) {
        try {
            Proxy proxy = null;
            IpLocale locale = null;
            String key = RedisKeyPrefixEnum.TASK_IP_LOCALE.getRedisKey(taskId);
            if (RedisUtils.exists(key)) {
                locale = JSON.parseObject(RedisUtils.get(key), new TypeReference<IpLocale>() {});
            }
            if (null == locale) {
                logger.warn("ip locale not found,taskId={}", taskId);
                proxy = proxyProvider.getProxy(taskId, websiteName);
            } else {
                logger.warn("ip locale found,taskId={},locale={}", taskId, JSON.toJSONString(locale));
                proxy = proxyProvider.getProxy(taskId, websiteName, locale.getProvinceName(), locale.getCityName());
            }

            if (null != proxy && StringUtils.isNoneBlank(proxy.getIp())) {
                logger.info("getProxyFromDubbo success,taskId={},websiteName={},proxy={}", taskId, websiteName, JSON.toJSONString(proxy));
                return proxy;
            }
        } catch (Exception e) {
            logger.debug("getProxyFromDubbo error,taskId={},websiteName={}", taskId, websiteName);
        }
        logger.warn("从dubbo获取代理失败,将使用本地网络,taskId={},websiteName={}", taskId, websiteName);
        return null;
    }

}
